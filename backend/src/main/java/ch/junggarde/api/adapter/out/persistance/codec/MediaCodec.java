package ch.junggarde.api.adapter.out.persistance.codec;

import ch.junggarde.api.model.media.FileType;
import ch.junggarde.api.model.media.MetaData;
import ch.junggarde.api.model.member.Member;
import com.mongodb.MongoClientSettings;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.UUID;

public class MediaCodec implements CollectibleCodec<MetaData> {
    private final Codec<Document> documentCodec;

    public MediaCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public MetaData decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new MetaData(
                UUID.fromString(document.getString(MetaData.Fields.id)),
                FileType.valueOf(document.getString(MetaData.Fields.type)),
                document.getString(MetaData.Fields.name),
                document.getInteger(MetaData.Fields.size)
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, MetaData metaData, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(Member.Fields.id, metaData.getId().toString())
                .append(MetaData.Fields.type, metaData.getType().toString())
                .append(MetaData.Fields.name, metaData.getName())
                .append(MetaData.Fields.size, metaData.getSize());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<MetaData> getEncoderClass() {
        return MetaData.class;
    }

    @Override
    public MetaData generateIdIfAbsentFromDocument(MetaData metaData) {
        return metaData;
    }

    @Override
    public boolean documentHasId(MetaData metaData) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(MetaData metaData) {
        return new BsonString(metaData.getId().toString());
    }
}
