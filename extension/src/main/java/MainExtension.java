import Authentication.LoginHandler;
import Authentication.RegistrationHandler;
import Database.DatabaseConnection;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MainExtension extends SFSExtension implements ISFSExtension {

    @Override
    public void init() {
        this.addRequestHandlers();
        this.databaseConnection();
    }

    private void addRequestHandlers() {
        addEventHandler(SFSEventType.USER_LOGIN, LoginHandler.class);
        addRequestHandler("RegistrationRequest", RegistrationHandler.class);
    }

    private void databaseConnection() {
        DatabaseConnection dataCon = new DatabaseConnection("localhost", 27017, "scratch", "users");
    }
}
