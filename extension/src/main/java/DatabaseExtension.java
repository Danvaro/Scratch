import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class DatabaseExtension extends SFSExtension implements ISFSExtension {

    private MongoClient mongoClient;
    public MongoDatabase local;

    @Override
    public void init() {
        databaseConnection();
    }

    private void handleError(Exception e) {
        trace(e);
    }

    private void databaseConnection() {
        try {
            trace("Connecting to database..");
            this.mongoClient = new MongoClient("localhost", 32772);
            trace("Database connected successful");

            try {
                trace("Getting Collections");
                MongoDatabase local = mongoClient.getDatabase("local");
                trace("Collections found");
            } catch (Exception e) {
                trace("Cant find collections. Creating them..");
                local.createCollection("users");
            }

        } catch (Exception e) {
            trace("Couldn't find to database or collections:");
            handleError(e);
        }
    }
}