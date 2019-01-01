package Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.UserDAO;
import Models.AuthToken;
import Models.Event;
import Models.User;
import Request.FillRequest;
import Request.RegisterRequest;
import Response.FillResponse;
import Response.RegisterResponse;

import static org.junit.Assert.*;

public class RegistrationServiceTest {
    DataBase db;
    ArrayList<User> users;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
        users = makeTestUsers();
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

    public ArrayList<User> makeTestUsers(){
        String u1 = "Tommy";
        String u2 = "George";
        String u3 = "bhales";
        String pw1 = "456";
        String pw2 = "No";
        String pw3 = "Mommy";
        String e1 = "a@gmail.com";
        String e2 = "b@gmail.com";
        String e3 = "c@gmail.com";
        String f1 = "Tom";
        String f2 = "georgy";
        String f3 = "B-rad";
        String l1 = "Thompson";
        String l2 = "Gonzalez";
        String l3 = "Hales";
        String m = "m";
        String f = "f";
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();

        ArrayList<User> users = new ArrayList<>();

        users.add(new User(u1, pw1, e1, f1, l1, m, pID1));
        users.add(new User(u2, pw2, e2, f2, l2, m, pID2));
        users.add(new User(u3, pw3, e3, f3, l3, m, pID3));

        return users;

    }

    @Test
    public void newRegistration() {
        // create a user
        String u1 = "Tommy";
        String pw1 = "456";
        String e1 = "a@gmail.com";
        String f1 = "Tom";
        String l1 = "Thompson";
        String m = "m";
        String pID1 = UUID.randomUUID().toString();
        try{
            db.openConnection();
            db.createTables();
            db.closeConnection(true);

            RegisterResponse response;
            RegisterRequest request = new RegisterRequest(u1, pw1, e1, f1, l1, m);

            RegistrationService service = new RegistrationService();

            response = service.newRegistration(request);

            AuthToken testAuth = response.getAuth();

            // make correct auth token is returned
            assertEquals(testAuth.getUserName(), "Tommy");

            // make sure there are 4 generations of data
            db.openConnection();
            EventDAO eDAO = db.getEvent();
            ArrayList<Event> testEvents = eDAO.getEvents("Tommy");
            db.closeConnection(true);
            assertNotNull(testEvents);
            int gen = 4;
            int numPeople = (int)(Math.pow(2.0,(double)(gen+1))) - 1;
            int numEvents = numPeople * 4;
            assertEquals(testEvents.size(), numEvents - 1);

            // insert bad data
            RegisterResponse response2;
            RegisterRequest request2 = new RegisterRequest(u1, null, e1, f1, null, m);

            RegistrationService service2 = new RegistrationService();

            response2 = service2.newRegistration(request2);
            assertEquals(response2.getMessage(), "Missing value");


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }
}