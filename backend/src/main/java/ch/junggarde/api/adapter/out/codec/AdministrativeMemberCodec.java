package ch.junggarde.api.adapter.out.codec;

import ch.junggarde.api.model.member.AdministrativeMember;
import ch.junggarde.api.model.member.Role;
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

public class AdministrativeMemberCodec implements CollectibleCodec<AdministrativeMember> {
    private final Codec<Document> documentCodec;

    public AdministrativeMemberCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public AdministrativeMember decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new AdministrativeMember(
                UUID.fromString(document.getString(AdministrativeMember.Fields.id)),
                UUID.fromString(document.getString(AdministrativeMember.Fields.memberId)),
                Role.valueOf(document.getString(AdministrativeMember.Fields.role)),
                document.getString(AdministrativeMember.Fields.jobTitle),
                document.getString(AdministrativeMember.Fields.description),
                UUID.fromString(document.getString(AdministrativeMember.Fields.imageId)),
                UUID.fromString(document.getString(AdministrativeMember.Fields.supervisorId))
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, AdministrativeMember administrativeMember, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(AdministrativeMember.Fields.id, administrativeMember.getId())
                .append(AdministrativeMember.Fields.memberId, administrativeMember.getMemberId())
                .append(AdministrativeMember.Fields.role, administrativeMember.getRole().toString())
                .append(AdministrativeMember.Fields.jobTitle, administrativeMember.getJobTitle())
                .append(AdministrativeMember.Fields.description, administrativeMember.getDescription())
                .append(AdministrativeMember.Fields.imageId, administrativeMember.getImageId())
                .append(AdministrativeMember.Fields.supervisorId, administrativeMember.getSupervisorId());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public AdministrativeMember generateIdIfAbsentFromDocument(AdministrativeMember administrativeMember) {
        return null;
    }

    @Override
    public boolean documentHasId(AdministrativeMember administrativeMember) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(AdministrativeMember administrativeMember) {
        return new BsonString(administrativeMember.getId().toString());
    }

    @Override
    public Class<AdministrativeMember> getEncoderClass() {
        return AdministrativeMember.class;
    }
} 