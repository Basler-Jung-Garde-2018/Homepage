package ch.junggarde.api.model.image;


import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class ImageNotFound extends RuntimeException {

    public ImageNotFound(UUID galleryImageId) {
        super("Image not found");
        log.error("Image of galleryImage {} not found", galleryImageId);
    }
}
