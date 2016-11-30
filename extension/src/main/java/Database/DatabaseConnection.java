package Database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.bson.Document;

public class DatabaseConnection extends SFSExtension implements ISFSExtension {

    private MongoClient mongoClient;
    private MongoDatabase scratch;
    public static MongoCollection<Document> users;

    @Override
    public void init() {

    }

    public DatabaseConnection(String host, int port, String database, String collection) {
        try {
            mongoClient = new MongoClient(host, port);
            scratch = mongoClient.getDatabase(database);
            DatabaseConnection.users = scratch.getCollection(collection);

        } catch (Exception e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {

        trace(e);
    }

    @Override
    public void destroy() {
        super.destroy();
        mongoClient.close();
        trace("Database connection -- stopped");
    }
}