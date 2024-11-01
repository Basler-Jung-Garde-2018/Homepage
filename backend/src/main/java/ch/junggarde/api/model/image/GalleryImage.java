package ch.junggarde.api.model.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Accessors(chain = true)
@Getter
@Setter
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class GalleryImage {
    private UUID id;
    private UUID imageId;
    private int year;
    private String event;

    public GalleryImage(UUID imageId, int year, String event) {
        this.id = UUID.randomUUID();
        this.imageId = imageId;
        this.year = year;
        this.event = event;
    }
}
