package Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Response.ClearResponse;

import static org.junit.Assert.*;

public class ClearServiceTest {
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
            d.printStackTrace();
        }
    }

    @Test
    public void clearAll() {
        // create Auth Token
        String n1 = "John";
        String ID1 = "345";
        AuthToken a1 = new AuthToken(n1, ID1);

        // create event
        String eID1 = UUID.randomUUID().toString();
        String desc1 = "Joey";
        String pID1 = UUID.randomUUID().toString();
        String lat1 = "345.7889";
        String lng1 = "900.00";
        String cnty = "USA";
        String cty1 = "Provo";
        String ev1 = "Baptism";
        String y1 = "1999";
        Event e1 = new Event(eID1, desc1, pID1, lat1, lng1, cnty, cty1, ev1, y1);

        // create person
        String ID = UUID.randomUUID().toString();
        String desc = "Brody";
        String name1 = "Brad";
        String last = "Williams";
        String m = "m";
        String father = "Jack";
        String mother = "Jill";
        Person p1 = new Person(ID, desc, name1, last, m, father, mother, null);

        // create user
        String u1 = "Tommy";
        String pw1 = "456";
        String evt1 = "a@gmail.com";
        String f1 = "Tom";
        String l1 = "Thompson";
        String pid1 = UUID.randomUUID().toString();
        User u = new User(u1, pw1, evt1, f1, l1, m, pid1);

        try{
            db.openConnection();
            db.createTables();

            EventDAO eDAO = db.getEvent();
            PersonDAO pDAO = db.getPerson();
            UserDAO uDAO = db.getUser();
            AuthTokenDAO aDAO = db.getAuth();

            eDAO.createEvent(e1);
            pDAO.createPerson(p1);
            uDAO.createUser(u);
            aDAO.createAuthToken(a1);

            Event eTest = eDAO.getEvent(e1.getEventID());
            Person pTest = pDAO.getPerson(p1.getPersonID());
            User uTest = uDAO.getUser(u.getUserName());
            AuthToken aTest = aDAO.getAuthToken(a1.getUUID());

            // make sure they are in the database
            assertNotNull(eTest);
            assertNotNull(pTest);
            assertNotNull(uTest);
            assertNotNull(aTest);

            db.closeConnection(true);

            // clear service
            ClearService clear = new ClearService();
            ClearResponse response;
            response =  clear.clearAll();


            db.openConnection();

            // make sure they are clear
            Event eT = eDAO.getEvent(e1.getEventID());
            Person pT = pDAO.getPerson(p1.getPersonID());
            User uT = uDAO.getUser(u.getUserName());
            AuthToken aT = aDAO.getAuthToken(a1.getUUID());
            // make sure they are in the database
            assertNull(eT);
            assertNull(pT);
            assertNull(uT);
            assertNull(aT);

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }
}