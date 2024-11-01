package ch.junggarde.api.application.dto.out;

import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.Image;

public record GalleryImageDTO(
        String id,
        String base64,
        int year,
        String event
) {
    public static GalleryImageDTO fromDomainModel(final GalleryImage galleryImage, final Image image) {
        return new GalleryImageDTO(
                galleryImage.getId().toString(),
                image.getBase64(),
                galleryImage.getYear(),
                galleryImage.getEvent()
        );
    }
}
