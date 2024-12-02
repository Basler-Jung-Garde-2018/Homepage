package ch.junggarde.api.application;

import ch.junggarde.api.model.media.FileType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class MediaService {
    @Inject
    @ConfigProperty(name = "variable.path.disk")
    String UPLOAD_DIRECTORY;

    public void uploadFiles(Map<String, InputStream> fileMap, FileType fileType) throws IOException {
        File directory = new File(UPLOAD_DIRECTORY + "/" + fileType.toString());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create upload directory");
        }

        for (Map.Entry<String, InputStream> entry : fileMap.entrySet()) {
            String filename = entry.getKey();
            InputStream fileStream = entry.getValue();

            log.info("Uploading file {} to {}/{}", filename, UPLOAD_DIRECTORY, fileType);

            // Define the output file path
            File outputFile = new File(directory, UUID.randomUUID() + filename);

            // Save the file to disk
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
