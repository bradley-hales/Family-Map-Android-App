package Services;

import java.net.Authenticator;
import java.util.HashMap;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.AuthToken;
import Models.Person;
import Models.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.PersonResponse;

public class LoginService {
    /**
     * Logs in the user and returns an auth token
     */



    public LoginService(){

    }


    /**
     * @return Authtoken object with unique auth token
     */
    public LoginResponse login(LoginRequest lr){
        /**
         * verifies the credentials of the user,
         * login user if successfully verified,
         * create auth token and return it
         */

        DataBase db = new DataBase();
        LoginResponse lRSP = new LoginResponse();
        try{
            db.openConnection();

            String username = lr.getUsername();
            String password = lr.getPassword();

            // check for missing properties
            if (username == null || password == null){
                lRSP.setMessage("Missing request property");
                db.closeConnection(true);
                return lRSP;
            }

            //check if username and password is valid
            UserDAO userDAO = db.getUser();
            User user = userDAO.getUser(username);
            if (user == null){
                lRSP.setMessage("Invalid username");
                db.closeConnection(true);
                return lRSP;
            }
            String passW = user.getPassword();
            if(!password.equals(passW)){
                lRSP.setMessage("Invalid password");
                db.closeConnection(true);
                return lRSP;
            }

            //Login success, create Authtoken
            AuthToken auth = new AuthToken(username, user.getPersonID());

            //put new authtoken in database
            AuthTokenDAO aDAO = db.getAuth();
            aDAO.createAuthToken(auth);


            //close connection
            db.closeConnection(true);

            lRSP.setAuth(auth);

        }catch (DataBaseException d){
            db.closeConnection(false);
            lRSP.setMessage("Error while logging in ");
        }

        return lRSP;
    }


}
