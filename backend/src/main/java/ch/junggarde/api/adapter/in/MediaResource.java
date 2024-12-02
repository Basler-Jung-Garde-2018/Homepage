package ch.junggarde.api.adapter.in;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/upload")
@Slf4j
public class MediaResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response uploadMedia(MultipartFormDataInput input) {
        log.info("Uploading file");
        try {
            // Retrieve the uploaded files
            Map<String, InputStream> fileMap = input.getFormDataMap()
                    .entrySet()
                    .stream()
                    .map(entry -> Map.entry(entry.getKey(), safeGetBody(entry.getValue().getFirst())))
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            StringBuilder fileDetails = new StringBuilder();
            for (Map.Entry<String, InputStream> entry : fileMap.entrySet()) {
                // Process the file without saving
                String fieldName = entry.getKey();
                InputStream fileStream = entry.getValue();
                fileDetails.append("Received file: ").append(fieldName)
                        .append(", Content Length: ").append(fileStream.available())
                        .append(" bytes\n");
            }

            return Response.ok(fileDetails.toString()).build();
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
