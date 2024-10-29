package ch.junggarde.api.adapter.out;

import ch.junggarde.api.model.image.Image;
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
public class ImageRepository {
    private static final String COLLECTION = "image";
    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public List<Image> findImagesByIds(List<String> imageIds) {
        return collection().find(Filters.in(Image.Fields.id, imageIds)).into(new ArrayList<>());
    }

    private MongoCollection<Image> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, Image.class);
    }

    public void saveImages(List<Image> images) {
        collection().insertMany(images);
    }
}
