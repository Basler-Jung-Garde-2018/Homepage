package ch.junggarde.api.adapter.out.codec;

import ch.junggarde.api.model.Appointment;
import ch.junggarde.api.model.AppointmentType;
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

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentCodec implements CollectibleCodec<Appointment> {
    private final Codec<Document> documentCodec;

    public AppointmentCodec() {
        this.documentCodec = MongoClientSettings.getDefaultCodecRegistry().get(Document.class);
    }

    @Override
    public Appointment decode(BsonReader bsonReader, DecoderContext decoderContext) {
        final Document document = documentCodec.decode(bsonReader, decoderContext);
        return new Appointment(
                UUID.fromString(document.getString(Appointment.Fields.id)),
                LocalDateTime.parse(document.getString(Appointment.Fields.date)),
                document.getString(Appointment.Fields.location),
                document.getString(Appointment.Fields.name),
                AppointmentType.valueOf(document.getString(Appointment.Fields.type))
        );
    }

    @Override
    public void encode(BsonWriter bsonWriter, Appointment appointment, EncoderContext encoderContext) {
        final Document document = new Document()
                .append(Appointment.Fields.id, appointment.getId().toString())
                .append(Appointment.Fields.date, appointment.getDate().toString())
                .append(Appointment.Fields.location, appointment.getLocation())
                .append(Appointment.Fields.name, appointment.getName())
                .append(Appointment.Fields.type, appointment.getType().toString());
        documentCodec.encode(bsonWriter, document, encoderContext);
    }

    @Override
    public Appointment generateIdIfAbsentFromDocument(Appointment appointment) {
        return null;
    }

    @Override
    public boolean documentHasId(Appointment appointment) {
        return true;
    }

    @Override
    public BsonValue getDocumentId(Appointment appointment) {
        return new BsonString(appointment.getId().toString());
    }

    @Override
    public Class<Appointment> getEncoderClass() {
        return Appointment.class;
    }
}
