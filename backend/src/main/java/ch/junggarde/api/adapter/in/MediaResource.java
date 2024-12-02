package ch.junggarde.api.adapter.in;

import ch.junggarde.api.application.MediaService;
import ch.junggarde.api.model.media.FileType;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/media")
@Slf4j
public class MediaResource {

    @Inject
    MediaService mediaService;

    @POST
    @Path("/{type}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMedia(MultipartFormDataInput input, @PathParam("type") String type) {
        log.info("Uploading file");
        try {
            // Retrieve the uploaded files
            Map<String, InputStream> fileMap = input.getFormDataMap()
                    .entrySet()
                    .stream()
                    .map(entry -> Map.entry(entry.getKey(), safeGetBody(entry.getValue().getFirst())))
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            this.mediaService.uploadFiles(fileMap, FileType.valueOf(type));

            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Failed to process file upload: " + e.getMessage())
                    .build();
        }
    }

    private InputStream safeGetBody(InputPart formDataValue) {
        try {
            return formDataValue.getBody(InputStream.class, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
