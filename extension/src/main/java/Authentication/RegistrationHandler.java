package Authentication;

import Database.DatabaseConnection;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class RegistrationHandler extends BaseClientRequestHandler {

    // TODO: Add class filter to check if user exists in database
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        String playerusername = params.getText("username");
        String playerpassword = params.getText("password");

        boolean doesuserexistindatabase;
        doesuserexistindatabase = findUserInDatabase(playerusername);

        if (doesuserexistindatabase) {
            sendRegistrationStatusBackToClient(sender, "User already exists");
        } else {
            // TODO: Encrypt password
            Document user = new Document("username", playerusername)
                    .append("password", playerpassword);
            DatabaseConnection.users.insertOne(user);

            sendRegistrationStatusBackToClient(sender, "Registration success");
        }
    }
    // TODO: Change find user to boolean check without returning data
    private boolean findUserInDatabase(String playerusername) {
        final Document user = DatabaseConnection.users.find(eq("username", playerusername)).first();
        return user != null;
    }

    private void sendRegistrationStatusBackToClient(User sender, String RegistrationStatus) {
        ISFSObject resObj = SFSObject.newInstance();
        resObj.putText("RegistrationStatus", RegistrationStatus);
        send("RegistrationResponse", resObj, sender);
    }
}