package ch.junggarde.api.model.image;


import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ImageNotFound extends RuntimeException {

    public ImageNotFound(UUID galleryImageId) {
        super("Image not found");
        log.error("Image of galleryImage {} not found", galleryImageId);
    }
}
