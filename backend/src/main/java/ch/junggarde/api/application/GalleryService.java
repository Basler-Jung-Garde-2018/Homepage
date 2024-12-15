package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.GalleryImageRepository;
import ch.junggarde.api.application.dto.GalleryImageDTO;
import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.ImageNotFound;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class GalleryService {
    @Inject
    GalleryImageRepository galleryImageRepository;

    @Inject
    @ConfigProperty(name = "variable.path.disk")
    String DIRECTORY;

    public List<UUID> getGallery(int year, String event) {
        return this.galleryImageRepository.findGalleryIds(year, event);
    }

    public List<GalleryImageDTO> addImages(List<GalleryImageDTO> imageMetadata) {
        if (imageMetadata.isEmpty()) {
            return new ArrayList<>();
        }
        List<GalleryImageDTO> response = new ArrayList<>(imageMetadata.size());
        List<GalleryImage> galleryImages = new ArrayList<>(imageMetadata.size());

        for (GalleryImageDTO galleryRequest : imageMetadata) {
            GalleryImage galleryImage = new GalleryImage(
                    UUID.fromString(galleryRequest.id()),
                    galleryRequest.year(),
                    galleryRequest.event(),
                    galleryRequest.published()
            );

            galleryImages.add(galleryImage);
            response.add(GalleryImageDTO.fromDomainModel(galleryImage));
        }

        galleryImageRepository.saveImages(galleryImages);

        return response;
    }

    public void publishImages(List<String> imageIds) {
        if (imageIds.isEmpty()) {
            return;
        }
        this.galleryImageRepository.publishImages(imageIds);

        // move files in other directory for better security
        imageIds.forEach(id -> {
            Path srcPath = Paths.get(DIRECTORY + "/IMAGE/" + id);
            Path destPath = Paths.get(DIRECTORY + "/PUBLIC/" + id);
            try {
                Files.move(srcPath, destPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public List<String> getEvents() {
        return this.galleryImageRepository.findEvents();
    }

    public File getImageFromDisk(UUID imageId) {
        File image = Paths.get(DIRECTORY, "PUBLIC", imageId.toString()).toFile();
        if (!image.exists() || !image.isFile()) {
            throw new ImageNotFound(imageId);
        }
        return image;
    }
}
