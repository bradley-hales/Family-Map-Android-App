package Services;

import java.util.UUID;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.UserDAO;
import Models.AuthToken;
import Models.User;
import Request.FillRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Response.LoginResponse;
import Response.RegisterResponse;

public class RegistrationService {
    /**
     * Register a new user
     */

    public RegistrationService(){}

    /**
     *
     * @param rR ReqisterRequest object
     * @return Auth token object verifying registration
     */
    public RegisterResponse newRegistration(RegisterRequest rR){
        /**
         * Takes as an argument a register request
         * of username and password and registers
         * the user creating a user and calls LoadService
         * to fill in data for 4 generations,
         * logs the user in by calling LoginService
         * and returns auth token
         */
        DataBase db = new DataBase();
        RegisterResponse rRSP = new RegisterResponse();
        try{
            db.openConnection();

            // make sure there are no missing values
            if (rR.getUserName() == null || rR.getFirstName() == null || rR.getLastName() == null
                    || rR.getPassword() == null || rR.getEmail() == null || rR.getGender() == null){

                db.closeConnection(false);
                rRSP.setMessage("Missing value");
                return rRSP;
            }
            if (!(rR.getGender().equals("m") || rR.getGender().equals("f"))){
                db.closeConnection(false);
                rRSP.setMessage("gender has to be either m or f");
                return rRSP;
            }

            // make sure username isn't taken
            UserDAO userDAO = db.getUser();
            User u = userDAO.getUser(rR.getUserName());

            if (u != null){
                db.closeConnection(false);
                rRSP.setMessage("Username already taken");
                return rRSP;
            }

            // create new user account
            String pID = UUID.randomUUID().toString();
            User user = new User(rR.getUserName(), rR.getPassword(), rR.getEmail(),
                                 rR.getFirstName(), rR.getLastName(), rR.getGender(), pID);
            UserDAO uDAO = db.getUser();
            uDAO.createUser(user);

            db.closeConnection(true);


            FillService fill = new FillService();
            FillRequest fillRequest = new FillRequest(rR.getUserName(), "4");
            fill.fill(fillRequest);

            // log user in
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest(rR.getUserName(), rR.getPassword());
            LoginResponse loginResponse = loginService.login(loginRequest);
            AuthToken auth = loginResponse.getAuth();

            rRSP.setAuth(auth);


            rRSP.setAuth(auth);

        }catch (DataBaseException d){
            db.closeConnection(false);
            rRSP.setMessage("Error while registering in ");
        }

        return rRSP;
    }
}
