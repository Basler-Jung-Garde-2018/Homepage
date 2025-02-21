package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.GalleryService;
import ch.junggarde.api.application.MediaService;
import ch.junggarde.api.application.dto.GalleryImageDTO;
import ch.junggarde.api.model.image.ImageNotFound;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Path("/gallery")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
@Slf4j
public class GalleryResource {
    @Inject
    GalleryService galleryService;
    @Inject
    MediaService mediaService;

    @GET
    @Path("/{year}/{event}")
    @PermitAll
    public Response getGallery(
            @PathParam("year") int year,
            @PathParam("event") String event
    ) {
        try {
            log.info("HTTP GET /gallery/{}/{}", year, event);
            List<UUID> galleryIds = this.galleryService.getGallery(year, event);
            return Response.ok().entity(this.mediaService.getMetaDataByIds(galleryIds)).build();
        } catch (ImageNotFound e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/image/{format}/{imageId}/{full}")
    @PermitAll
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getGalleryImage(@PathParam("format") String format, @PathParam("imageId") String imageId, @PathParam("full") boolean full) {
        log.info("HTTP GET /gallery/{}", imageId);
        try {
            return Response.ok((StreamingOutput) output -> {
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    File file = this.galleryService.getImageFromDisk(UUID.fromString(imageId));

                    BufferedImage image = Thumbnails.of(new FileInputStream(file))
                            .scale(1)
                            .asBufferedImage();

                    if (!full && image.getHeight() > 1080) {
                        image = Thumbnails.of(new FileInputStream(file))
                                .size((1080 * image.getWidth()) / image.getHeight(), 1080)
                                .asBufferedImage();
                    }

                    ImageIO.write(image, format, baos);
                    baos.flush();

                    InputStream scaledInputStream = new ByteArrayInputStream(baos.toByteArray());
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = scaledInputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new WebApplicationException(e);
                }
            }).build();
        } catch (ImageNotFound e) {
            return Response.serverError().status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @RolesAllowed("mitglied")
    public Response addImageMetaData(List<GalleryImageDTO> imageMetadata) {
        log.info("HTTP POST /gallery metadata.event: {}, metadata.year: {}", imageMetadata.getFirst().event(), imageMetadata.getFirst().year());
        log.info("add {} Images", imageMetadata.size());
        return Response.ok().entity(galleryService.addImages(imageMetadata)).build();
    }

    @PUT
    @RolesAllowed("mitglied")
    public Response publishImages(List<String> imageIds) {
        log.info("HTTP PUT /gallery {}", imageIds);
        log.info("publish {} Images", imageIds.size());
        galleryService.publishImages(imageIds);
        return Response.ok().build();
    }

    @GET
    @Path("/events")
    @PermitAll
    public List<String> getEvents() {
        log.info("HTTP GET /gallery/events");
        return galleryService.getEvents();
    }
}
