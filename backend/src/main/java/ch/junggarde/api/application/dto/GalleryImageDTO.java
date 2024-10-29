package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.image.GalleryImage;
import ch.junggarde.api.model.image.Image;

public record GalleryImageDTO(
        String id,
        String format,
        String base64,
        int year,
        String event,
        String positionId
) {
    public static GalleryImageDTO fromDomainModel(final GalleryImage galleryImage, final Image image) {
        return new GalleryImageDTO(
                galleryImage.getId().toString(),
                image.getFormat(),
                image.getBase64(),
                galleryImage.getYear(),
                galleryImage.getEvent(),
                galleryImage.getPositionId().toString()
        );
    }
}
