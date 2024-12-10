package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.GalleryImageRepository;
import ch.junggarde.api.adapter.out.persistance.ImageRepository;
import ch.junggarde.api.application.dto.GalleryImageDTO;
import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.image.ImageNotFound;
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

    @Inject
    ImageRepository imageRepository;

    public List<String> getGallery(int year, String event, int page) {
        return this.galleryImageRepository.findGalleryIds(year, event, page).stream().map(UUID::toString).toList();
    }

    public GalleryImageDTO getGalleryData(UUID imageId) throws ImageNotFound {
        GalleryImage galleryImage = galleryImageRepository.findGalleryImageById(imageId);

        Image image = imageRepository.findById(galleryImage.getImageId());

        return GalleryImageDTO.fromDomainModel(galleryImage, image);
    }

    public List<GalleryImageDTO> addImages(List<GalleryImageDTO> galleryRequests) {
        if (galleryRequests.isEmpty()) {
            return new ArrayList<>();
        }
        List<GalleryImageDTO> response = new ArrayList<>(galleryRequests.size());
        List<GalleryImage> galleryImages = new ArrayList<>(galleryRequests.size());
        List<Image> images = new ArrayList<>(galleryRequests.size());

        for (GalleryImageDTO galleryRequest : galleryRequests) {
            Image image = new Image(galleryRequest.base64());
            GalleryImage galleryImage = new GalleryImage(
                    image.getId(),
                    galleryRequest.year(),
                    galleryRequest.event()
            );

            images.add(image);
            galleryImages.add(galleryImage);
            response.add(GalleryImageDTO.fromDomainModel(galleryImage, image));
        }

        imageRepository.saveImages(images);
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
