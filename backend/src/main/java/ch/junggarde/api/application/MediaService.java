package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.MetaDataRepository;
import ch.junggarde.api.model.media.FileType;
import ch.junggarde.api.model.media.MediaNotFound;
import ch.junggarde.api.model.media.MetaData;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class MediaService {

    @Inject
    MetaDataRepository metaDataRepository;

    @Inject
    @ConfigProperty(name = "variable.path.disk")
    String DIRECTORY;

    public void uploadFiles(MultipartFormDataInput input, FileType fileType) throws IOException {
        // Parse input
        Map<String, InputStream> fileMap = input.getFormDataMap()
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), safeGetBody(entry.getValue().getFirst())))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Check directory
        File directory = new File(DIRECTORY + "/" + fileType.toString());
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create upload directory");
        }

        // Save file to disk
        List<MetaData> metaData = new ArrayList<>(fileMap.entrySet().size());
        for (Map.Entry<String, InputStream> entry : fileMap.entrySet()) {
            String filename = entry.getKey();
            InputStream fileStream = entry.getValue();

            log.info("Uploading file {} to {}/{}", filename, DIRECTORY, fileType);

            // Define the output file path
            UUID mediaId = UUID.randomUUID();
            File outputFile = new File(directory, mediaId.toString());

            int fileSize = 0;
            // Save the file to disk
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    fileSize += bytesRead;
                }
            }
            metaData.add(new MetaData(mediaId, fileType, filename, fileSize));
        }
        // Save metadata to db
        this.metaDataRepository.saveMetaData(metaData);
    }

    public List<MetaData> getMetaData(FileType fileType) {
        return this.metaDataRepository.findMetaDataByType(fileType);
    }

    public File getMediaFromDisk(String type, UUID mediaId) {
        File file = Paths.get(DIRECTORY + "/" + type + "/" + mediaId.toString()).toFile();
        if (!file.exists() || !file.isFile()) {
            throw new MediaNotFound(mediaId);
        }
        return file;
    }


    private InputStream safeGetBody(InputPart formDataValue) {
        try {
            return formDataValue.getBody(InputStream.class, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
