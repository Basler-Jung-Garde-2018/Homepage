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
public class Image {
    private UUID id;
    private String format;
    private String base64;

    public Image(String format, String base64) {
        this.id = UUID.randomUUID();
        this.format = format;
        this.base64 = base64;
    }
}
