package Authentication;

import Database.DatabaseConnection;
import com.mongodb.MongoException;
import com.mongodb.annotations.ThreadSafe;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class LoginHandler extends BaseServerEventHandler {


    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {

        ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);
        String playerusername = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
        String playerpassword = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);
        String password;

        SFSObject sfso = (SFSObject) event.getParameter(SFSEventParam.LOGIN_IN_DATA);

        if (sfso != null) {
            return;
        }

        if (playerusername.equals("")) {
            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
            data.addParameter(playerusername);
            throw new SFSLoginException("No username specified.", data);

        }

        if (playerpassword.equals("")) {
            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
            data.addParameter(playerpassword);
            throw new SFSLoginException("No password specified.", data);
        }

        Document user = DatabaseConnection.users.find(eq("username", playerusername)).first();
        if (user == null) {
            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
            data.addParameter(playerusername);
            throw new SFSLoginException("Username not found in database.", data);
        }
        password = user.get("password").toString();

        if (!getApi().checkSecurePassword(session, password, playerpassword)) {

            SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
            data.addParameter(playerpassword);
            throw new SFSLoginException("Password incorrect", data);
        }
    }
}



