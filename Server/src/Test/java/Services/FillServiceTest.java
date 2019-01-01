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
import Request.LoginRequest;
import Response.FillResponse;
import Response.LoginResponse;

import static org.junit.Assert.*;

public class FillServiceTest {
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
    public void fill() {
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

            db.openConnection();
            FillRequest request = new FillRequest(u.getUserName());
            FillResponse response;

            FillService service = new FillService();

            response = service.fill(request);

            // make sure right number of events
            String generation = request.getGenerations();
            int gen = Integer.valueOf(generation);
            int numPeople = (int)(Math.pow(2.0,(double)(gen+1))) - 1;
            int numEvents = numPeople * 4;

            EventDAO eDAO = db.getEvent();
            ArrayList<Event> testEvents = eDAO.getEvents("Tommy");
            assertNotNull(testEvents);
            assertEquals(testEvents.size(), numEvents - 1);
            db.clearAll();
            db.closeConnection(true);

            // bad data, num of generations less than 0
            FillRequest request2 = new FillRequest(u.getUserName(), "-1");
            FillResponse response2;

            FillService service2 = new FillService();

            response2 = service2.fill(request2);

            assertEquals(response2.getMessage(), "generations cant be negative");



        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }
}