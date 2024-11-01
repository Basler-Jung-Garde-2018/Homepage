package ch.junggarde.api.adapter.out.persistance.codec;

import ch.junggarde.api.model.image.GalleryImage;
import com.mongodb.MongoClientSettings;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class GalleryImageCodec implements CollectibleCodec<GalleryImage> {
    private final Codec<Document> documentCodec;

    public GalleryImageCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public GalleryImage decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);

        return new GalleryImage(
                UUID.fromString(document.getString(GalleryImage.Fields.id)),
                UUID.fromString(document.getString(GalleryImage.Fields.imageId)),
                document.getInteger(GalleryImage.Fields.year),
                document.getString(GalleryImage.Fields.event)
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, GalleryImage galleryImage, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(GalleryImage.Fields.id, galleryImage.getId().toString())
                .append(GalleryImage.Fields.imageId, galleryImage.getImageId().toString())
                .append(GalleryImage.Fields.year, galleryImage.getYear())
                .append(GalleryImage.Fields.event, galleryImage.getEvent());

        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<GalleryImage> getEncoderClass() {
        return GalleryImage.class;
    }

    @Override
    public GalleryImage generateIdIfAbsentFromDocument(GalleryImage galleryImage) {
        return galleryImage;
    }

    @Override
    public boolean documentHasId(GalleryImage galleryImage) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(GalleryImage galleryImage) {
        return new BsonString(galleryImage.getId().toString());
    }
}
