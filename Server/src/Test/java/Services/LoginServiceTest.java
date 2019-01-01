package Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import DAO.DataBase;
import DAO.DataBaseException;
import DAO.UserDAO;
import Models.AuthToken;
import Models.User;
import Request.LoginRequest;
import Response.LoginResponse;

import static org.junit.Assert.*;

public class LoginServiceTest {
    DataBase db;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
    }

    @After
    public void tearDown() throws Exception {
        try{
            db.openConnection();
            db.clearAll();

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void login() {
        // create a user
        String u1 = "Tommy";
        String pw1 = "456";
        String e1 = "a@gmail.com";
        String f1 = "Tom";
        String l1 = "Thompson";
        String m = "m";
        String pID1 = UUID.randomUUID().toString();
        User u = new User(u1, pw1, e1, f1, l1, m, pID1);
        try{
            db.openConnection();
            db.createTables();

            // put user in database
            UserDAO uDAO = db.getUser();
            uDAO.createUser(u);
            db.closeConnection(true);
            db.openConnection();
            User testU = uDAO.getUser(u.getUserName());
            assertNotNull(testU);
            db.closeConnection(true);

            LoginRequest request = new LoginRequest(u.getUserName(), u.getPassword());
            LoginResponse response;

            LoginService service = new LoginService();

            response = service.login(request);

            AuthToken auth = response.getAuth();
            assertNotNull(auth);
            assertEquals(auth.getPersonID(), u.getPersonID());
            assertEquals(auth.getUserName(), u.getUserName());

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }
}