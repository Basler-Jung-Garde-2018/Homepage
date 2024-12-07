package ch.junggarde.api.model.media;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class MediaNotFound extends RuntimeException {
    public MediaNotFound(UUID imageId) {
        super("Media not found");
        log.error("Media with ID {} not found", imageId);
    }
}