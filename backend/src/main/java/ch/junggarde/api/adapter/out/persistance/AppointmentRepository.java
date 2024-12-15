package ch.junggarde.api.adapter.out.persistance;

import ch.junggarde.api.model.Appointment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AppointmentRepository {
    private static final String COLLECTION = "appointment";
    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public void saveAppointment(Appointment appointments) {
        collection().insertOne(appointments);
    }

    public List<Appointment> findAll() {
        return collection().find().into(new ArrayList<>());
    }

    public List<Appointment> findAllPublic() {
        Bson filter = Filters.eq(Appointment.Fields.published, true);
        return collection().find(filter).into(new ArrayList<>());
    }

    private MongoCollection<Appointment> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, Appointment.class);
    }
}
