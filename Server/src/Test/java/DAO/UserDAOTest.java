package DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import Models.AuthToken;
import Models.Event;
import Models.User;

import static org.junit.Assert.*;

public class UserDAOTest {
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
    public void getUser() {
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            UserDAO uDAO = db.getUser();

            for(User u: users){
                uDAO.createUser(u);
            }

            User testU = uDAO.getUser("George");
            assertNotNull(testU);
            assertEquals("George", testU.getUserName());

            // try to get user not in database
            User testU2 = uDAO.getUser("crazy");
            assertNull(testU2);



            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

    @Test
    public void createUser() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            UserDAO uDAO = db.getUser();

            for(User u: users){
                uDAO.createUser(u);
                User test = uDAO.getUser(u.getUserName());
                assertNotNull(test);
                assertEquals(test.getUserName(), u.getUserName());
                assertEquals(test.getPassword(), u.getPassword());
                assertEquals(test.getEmail(), u.getEmail());
                assertEquals(test.getFirstName(), u.getFirstName());
                assertEquals(test.getLastName(), u.getLastName());
                assertEquals(test.getGender(), u.getGender());
                assertEquals(test.getPersonID(), u.getPersonID());
            }





            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

    @Test
    public void clear() {
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            UserDAO uDAO = db.getUser();


            for(User u: users){
                uDAO.createUser(u);
            }

            // make sure table not empty
            User u = uDAO.getUser("bhales");
            assertNotNull(u);

            uDAO.clear();
            User testU = uDAO.getUser("bhales");
            assertNull(testU);

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }


}