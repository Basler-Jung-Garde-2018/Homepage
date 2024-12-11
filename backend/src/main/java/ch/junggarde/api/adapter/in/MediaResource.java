package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.MediaService;
import ch.junggarde.api.model.media.FileType;
import ch.junggarde.api.model.media.MediaNotFound;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Path("/media")
@Slf4j
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class MediaResource {

    @Inject
    MediaService mediaService;

    @POST
    @Path("/{type}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMedia(MultipartFormDataInput input, @PathParam("type") String type) {
        log.info("HTTP POST /media/{} {}", type, input);
        try {
            return Response.ok(this.mediaService.uploadFiles(input, FileType.valueOf(type))).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Failed to process file upload: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{type}")
    public Response getMetaData(@PathParam("type") String type) {
        log.info("HTTP GET /media/{}", type);
        try {
            return Response.ok(this.mediaService.getMetaData(FileType.valueOf(type))).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{type}/{mediaId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getMedia(@PathParam("type") String type, @PathParam("mediaId") String mediaId) {
        log.info("HTTP GET /media/{}/{}", type, mediaId);
        try {
            File file = this.mediaService.getMediaFromDisk(type, UUID.fromString(mediaId));
            return Response.ok((StreamingOutput) output -> {
                try (InputStream inputStream = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new WebApplicationException(e);
                }
            }).build();
        } catch (MediaNotFound e) {
            return Response.serverError().status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}
