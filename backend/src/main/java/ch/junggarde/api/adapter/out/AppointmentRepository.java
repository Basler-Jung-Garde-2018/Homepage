package ch.junggarde.api.adapter.out;

import ch.junggarde.api.model.Appointment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
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

    public void save(Appointment appointment) {
        collection().insertOne(appointment);
    }

    public List<Appointment> findAll() {
        return collection().find().into(new ArrayList<>());
    }

    private MongoCollection<Appointment> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, Appointment.class);
    }
}
