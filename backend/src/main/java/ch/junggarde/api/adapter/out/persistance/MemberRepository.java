package ch.junggarde.api.adapter.out.persistance;

import ch.junggarde.api.model.image.Image;
import ch.junggarde.api.model.member.Member;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemberRepository {
    private static final String COLLECTION = "member";
    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public List<Member> findAdministrativeMembers(List<String> memberIds) {
        return collection().find(Filters.in(Image.Fields.id, memberIds)).into(new ArrayList<>());
    }

    private MongoCollection<Member> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, Member.class);
    }

    public List<Member> findAll() {
        return collection().find().into(new ArrayList<>());
    }

    public void saveMembers(List<Member> members) {
        collection().insertMany(members);
    }
}
