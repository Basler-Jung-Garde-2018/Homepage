package ch.junggarde.api.application;

import ch.junggarde.api.adapter.out.persistance.GalleryImageRepository;
import ch.junggarde.api.adapter.out.persistance.ImageRepository;
import ch.junggarde.api.application.dto.in.AddToGalleryRequest;
import ch.junggarde.api.application.dto.out.GalleryImageDTO;
import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.image.ImageNotFound;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Log4j2
public class GalleryService {
    @Inject
    GalleryImageRepository galleryImageRepository;

    @Inject
    ImageRepository imageRepository;


    public List<GalleryImageDTO> getGallery(int year, String event, int page) throws ImageNotFound {
        List<GalleryImage> galleryImages = galleryImageRepository.getGallery(year, event, page);

        // Get all imageIds of the gallery images
        List<String> imageIds = galleryImages.stream()
                .map(galleryImage -> galleryImage.getImageId().toString())
                .toList();

        List<Image> images = imageRepository.findImagesByIds(imageIds);

        // Add image to gallery image and map to dto
        return galleryImages.stream()
                .map(galleryImage -> GalleryImageDTO.fromDomainModel(
                                galleryImage,
                                images.stream()
                                        .filter(image -> image.getId().equals(galleryImage.getImageId()))
                                        .findFirst()
                                        .orElseThrow(() -> new ImageNotFound(galleryImage.getId()))
                        )
                ).toList();
    }

    public List<GalleryImageDTO> addImages(List<AddToGalleryRequest> galleryRequests) {
        List<GalleryImageDTO> response = new ArrayList<>(galleryRequests.size());
        List<GalleryImage> galleryImages = new ArrayList<>(galleryRequests.size());
        List<Image> images = new ArrayList<>(galleryRequests.size());

        for (AddToGalleryRequest galleryRequest : galleryRequests) {
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
}
