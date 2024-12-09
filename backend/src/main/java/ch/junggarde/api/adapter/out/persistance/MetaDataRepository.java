package ch.junggarde.api.adapter.out.persistance;

import ch.junggarde.api.model.media.FileType;
import ch.junggarde.api.model.media.MetaData;
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
public class MetaDataRepository {
    private static final String COLLECTION = "metadata";

    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    private MongoCollection<MetaData> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, MetaData.class);
    }

    public void saveMetaData(List<MetaData> metaData) {
        collection().insertMany(metaData);
    }

    public List<MetaData> findMetaDataByType(FileType fileType) {
        Bson filter = Filters.eq(MetaData.Fields.type, fileType);
        return collection().find(filter).into(new ArrayList<>());
    }
}
