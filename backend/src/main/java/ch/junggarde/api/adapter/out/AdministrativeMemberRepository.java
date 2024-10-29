package ch.junggarde.api.adapter.out;

import ch.junggarde.api.model.member.AdministrativeMember;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AdministrativeMemberRepository {
    private static final String COLLECTION = "administrativeMember";
    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public void save(AdministrativeMember administrativeMember) {
        collection().insertOne(administrativeMember);
    }

    public List<AdministrativeMember> findAll() {
        return collection().find().into(new ArrayList<>());
    }

    private MongoCollection<AdministrativeMember> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, AdministrativeMember.class);
    }
}
