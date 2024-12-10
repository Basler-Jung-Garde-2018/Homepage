package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.GalleryService;
import ch.junggarde.api.application.dto.GalleryImageDTO;
import ch.junggarde.api.model.image.ImageNotFound;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

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

    @GET
    @Path("/{year}/{event}/{page}")
    public Response getGallery(
            @PathParam("year") int year,
            @PathParam("event") String event,
            @PathParam("page") int page
    ) {
        try {
            log.info("HTTP GET /gallery/{}/{}/{}", year, event, page);
            return Response.ok().entity(this.galleryService.getGallery(year, event, page)).build();
        } catch (ImageNotFound e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{imageId}")
    public Response getGalleryImage(@PathParam("imageId") String imageId) {
        log.info("HTTP GET /gallery/{}", imageId);
        try {
            return Response.ok().entity(this.galleryService.getGalleryData(UUID.fromString(imageId))).build();
        } catch (Exception e) {
            log.error("[{}] {}", imageId, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response addImages(List<GalleryImageDTO> images) {
        log.info("HTTP POST /gallery");
        log.info("add {} Images", images.size());
        return Response.ok().entity(galleryService.addImages(images)).build();
    }

    @PUT
    public Response publishImages(List<String> imageIds) {
        log.info("HTTP PUT /gallery {}", imageIds);
        log.info("publish {} Images", imageIds.size());
        galleryService.publishImages(imageIds);
        return Response.ok().build();
    }

    @GET
    @Path("/events")
    public List<String> getEvents() {
        log.info("HTTP GET /gallery/events");
        return galleryService.getEvents();
    }
}
