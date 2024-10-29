package ch.junggarde.api.adapter.out.codec;

import ch.junggarde.api.model.member.Function;
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

public class MemberCodec implements CollectibleCodec<Member> {
    private final Codec<Document> documentCodec;

    public MemberCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Member decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new Member(
                UUID.fromString(document.getString(Member.Fields.id)),
                document.getString(Member.Fields.firstname),
                document.getString(Member.Fields.lastname),
                Function.valueOf(document.getString(Member.Fields.function))
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, Member member, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(Member.Fields.id, member.getId().toString())
                .append(Member.Fields.firstname, member.getFirstname())
                .append(Member.Fields.lastname, member.getLastname())
                .append(Member.Fields.function, member.getFunction().toString());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Class<Member> getEncoderClass() {
        return Member.class;
    }

    @Override
    public Member generateIdIfAbsentFromDocument(Member member) {
        return member;
    }

    @Override
    public boolean documentHasId(Member member) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(Member member) {
        return new BsonString(member.getId().toString());
    }
}
