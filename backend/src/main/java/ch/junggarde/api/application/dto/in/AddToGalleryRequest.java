package ch.junggarde.api.application.dto.in;

public record AddToGalleryRequest(
        String base64,
        int year,
        String event
) {

}
