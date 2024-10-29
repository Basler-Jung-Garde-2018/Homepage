package ch.junggarde.api.adapter.out.codec;

import ch.junggarde.api.model.media.Media;
import ch.junggarde.api.model.media.MediaType;
import ch.junggarde.api.model.member.Member;
import com.mongodb.MongoClientSettings;
import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class MediaCodec implements CollectibleCodec<Media> {
    private final Codec<Document> documentCodec;

    public MediaCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Media decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new Media(
                UUID.fromString(document.getString(Media.Fields.id)),
                MediaType.valueOf(document.getString(Media.Fields.type)),
                UUID.fromString(document.getString(Media.Fields.mediaId))
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, Media media, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(Member.Fields.id, media.getId().toString())
                .append(Media.Fields.type, media.getType().toString())
                .append(Media.Fields.mediaId, media.getMediaId());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Media> getEncoderClass() {
        return Media.class;
    }

    @Override
    public Media generateIdIfAbsentFromDocument(Media media) {
        return media;
    }

    @Override
    public boolean documentHasId(Media media) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(Media media) {
        return new BsonString(media.getId().toString());
    }
}
