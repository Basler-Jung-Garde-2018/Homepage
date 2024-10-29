package ch.junggarde.api.adapter.out;

import ch.junggarde.api.model.image.GalleryImage;
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
public class GalleryImageRepository {
    private static final String COLLECTION = "galleryImage";

    @Inject
    Instance<MongoClient> mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    public List<GalleryImage> getGallery(int year, String event, int page) {
        int docOnPage = page == 0 ? 40 : 20;
        Bson filter = Filters.and(
                Filters.eq(GalleryImage.Fields.year, year),
                Filters.eq(GalleryImage.Fields.event, event)
        );
        return collection().find(filter)
                .skip(page * docOnPage)
                .limit(docOnPage)
                .into(new ArrayList<>());
    }

    private MongoCollection<GalleryImage> collection() {
        return mongoClient.get().getDatabase(database).getCollection(COLLECTION, GalleryImage.class);
    }

    public void saveImages(List<GalleryImage> galleryImages) {
        collection().insertMany(galleryImages);
    }
}
