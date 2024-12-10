package ch.junggarde.api.adapter.out.persistance;

import ch.junggarde.api.model.image.GalleryImage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
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

    public GalleryImage findGalleryImageById(UUID imageId) {
        Bson filter = Filters.eq(GalleryImage.Fields.id, imageId.toString());
        return collection().find(filter).first();
    }

    public void saveImages(List<GalleryImage> galleryImages) {
        collection().insertMany(galleryImages);
    }

    public void publishImages(List<String> imageIds) {
        Bson filter = Filters.in(GalleryImage.Fields.id, imageIds);
        Bson update = Updates.set(GalleryImage.Fields.published, true);
        collection().updateMany(filter, update, new UpdateOptions().upsert(false));
    }

    public List<UUID> findGalleryIds(int year, String event, int page) {
        int docOnPage = 20;
        if (page == 0) {
            docOnPage = 40;
        } else {
            page += 1;
        }
        Bson filter = Filters.and(
                Filters.eq(GalleryImage.Fields.year, year),
                Filters.eq(GalleryImage.Fields.event, event),
                Filters.eq(GalleryImage.Fields.published, true)
        );

        return collection().find(filter)
                .skip(page * docOnPage)
                .limit(docOnPage)
                .map(GalleryImage::getId)
                .into(new ArrayList<>());
    }
}
