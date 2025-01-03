package ch.junggarde.api.adapter.out.persistance;

import ch.junggarde.api.model.image.GalleryImage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GalleryImageRepository {
    private static final String COLLECTION = "galleryImage";

    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    private MongoCollection<GalleryImage> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, GalleryImage.class);
    }

    private MongoCollection<Document> filterCollection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, Document.class);
    }

    public List<UUID> findGalleryIds(int year, String event) {
        Bson filter = Filters.and(
                Filters.eq(GalleryImage.Fields.year, year),
                Filters.eq(GalleryImage.Fields.event, event),
                Filters.eq(GalleryImage.Fields.published, true)
        );

        return filterCollection().find(filter)
                .projection(Projections.fields(
                        Projections.include("id"),
                        Projections.excludeId()
                ))
                .map(document -> UUID.fromString(document.get("id", String.class)))
                .into(new ArrayList<>());
    }

    public void saveImages(List<GalleryImage> galleryImages) {
        collection().insertMany(galleryImages);
    }

    public void publishImages(List<String> imageIds) {
        Bson filter = Filters.in(GalleryImage.Fields.id, imageIds);
        Bson update = Updates.set(GalleryImage.Fields.published, true);
        collection().updateMany(filter, update, new UpdateOptions().upsert(false));
    }

    public List<String> findEvents() {
        Bson filter = Filters.eq(GalleryImage.Fields.published, true);
        return filterCollection()
                .distinct("event", filter, String.class)
                .into(new ArrayList<>());
    }
}
