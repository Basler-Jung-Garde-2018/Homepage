package ch.junggarde.api.application.dto;

import ch.junggarde.api.model.image.GalleryImage;

public record GalleryImageDTO(
        String id,
        int year,
        String event,
        Boolean published
) {
    public static GalleryImageDTO fromDomainModel(final GalleryImage galleryImage) {
        return new GalleryImageDTO(
                galleryImage.getId().toString(),
                galleryImage.getYear(),
                galleryImage.getEvent(),
                galleryImage.isPublished()
        );
    }
}
