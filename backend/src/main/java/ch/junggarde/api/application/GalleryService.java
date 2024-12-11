package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.GalleryImageRepository;
import ch.junggarde.api.application.dto.GalleryImageDTO;
import ch.junggarde.api.model.image.GalleryImage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Slf4j
public class GalleryService {
    @Inject
    GalleryImageRepository galleryImageRepository;

    public List<String> getGallery(int year, String event, int page) {
        return this.galleryImageRepository.findGalleryIds(year, event, page).stream().map(UUID::toString).toList();
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
                    galleryRequest.event()
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
    }

    public List<String> getEvents() {
        return this.galleryImageRepository.findEvents();
    }
}
