package ch.junggarde.api.adapter.out.persistance.codec;

import ch.junggarde.api.model.image.Image;
import com.mongodb.MongoClientSettings;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class ImageCodec implements CollectibleCodec<Image> {
    private final Codec<Document> documentCodec;

    public ImageCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Image decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new Image(
                UUID.fromString(document.getString(Image.Fields.id)),
                document.getString(Image.Fields.base64)
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, Image image, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(Image.Fields.id, image.getId().toString())
                .append(Image.Fields.base64, image.getBase64());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Image> getEncoderClass() {
        return Image.class;
    }

    @Override
    public Image generateIdIfAbsentFromDocument(Image image) {
        return image;
    }

    @Override
    public boolean documentHasId(Image image) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(Image image) {
        return new BsonString(image.getId().toString());
    }
}
