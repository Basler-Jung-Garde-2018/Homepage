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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
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

    public GalleryImageDTO getGalleryData(UUID imageId) throws ImageNotFound, IOException {
        GalleryImage galleryImage = galleryImageRepository.findGalleryImageById(imageId);

        Image image = imageRepository.findById(galleryImage.getImageId());
        checkAndDownScaleImage(image);

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

    private static void checkAndDownScaleImage(Image image) throws IOException {
        int frontIndex = image.getBase64().indexOf(",") + 1;
        String base64Front = image.getBase64().substring(0, frontIndex);
        byte[] imageBytes = Base64.getDecoder().decode(image.getBase64().substring(frontIndex));

        if (imageBytes.length > 2_000_000) {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageBytes));

            BufferedImage resized = resize(original, 1080 * original.getWidth() / original.getHeight(), 1080);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String fileType = base64Front.substring(base64Front.indexOf("/") + 1, base64Front.indexOf(";"));
            ImageIO.write(resized, fileType, baos);
            imageBytes = baos.toByteArray();

            String downscaledBase64Image = Base64.getEncoder().encodeToString(imageBytes);
            image.setBase64(base64Front + downscaledBase64Image);
        }
    }

    private static BufferedImage resize(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
}
