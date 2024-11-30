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
            log.info("Returning gallery. Year: {}, event: {}, page: {}", year, event, page);
            return Response.ok().entity(this.galleryService.getGallery(year, event, page)).build();
        } catch (ImageNotFound e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    public Response addImages(List<GalleryImageDTO> images) {
        log.info("add {} Images", images.size());
        return Response.ok().entity(galleryService.addImages(images)).build();
    }

    @PUT
    public Response publishImages(List<String> imageIds) {
        log.info("publish {} Images", imageIds.size());
        galleryService.publishImages(imageIds);
        return Response.ok().build();
    }
}
