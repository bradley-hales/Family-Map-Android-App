package DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import Models.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    DataBase db;

    @Before
    public void setUp() throws Exception {
        System.out.println("Starting test");
        db = new DataBase();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Test finished");
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
    public void createAuthToken() {
        System.out.println("creating auth token");

        String u1 = "brad";
        String u1ID = "1234";
        String u2 = "joe";
        String u2ID = "45676";
        AuthToken a1 = new AuthToken(u1, u1ID);
        AuthToken a2 = new AuthToken(u2, u2ID);
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            AuthTokenDAO authDAO = db.getAuth();
            authDAO.createAuthToken(a1);
            authDAO.createAuthToken(a2);
            String authTok1 = a1.getUUID();
            String authTok2 = a2.getUUID();
            System.out.println(a1.getUUID());

            AuthToken test1 = authDAO.getAuthToken(authTok1);
            AuthToken test2 = authDAO.getAuthToken(authTok2);

            assertEquals(authTok1, test1.getUUID());
            assertEquals(authTok2, test2.getUUID());



            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

    @Test
    public void getAuthToken() {

        System.out.println("getting auth");
        try{
            db.openConnection();
            String name1 = "John";
            String name2 = "Bob";
            String pID1 = "9494";
            String pID2 = "9877";

            AuthToken a1 = new AuthToken(name1, pID1);
            AuthToken a2 = new AuthToken(name2, pID2);

            AuthTokenDAO aDAO = db.getAuth();

            aDAO.createAuthToken(a1);
            aDAO.createAuthToken(a2);

            AuthToken auth1 = aDAO.getAuthToken(a1.getUUID());
            AuthToken auth2 = aDAO.getAuthToken(a2.getUUID());

            System.out.println("testing if auth token is null");
            assertNotNull(auth1);
            assertNotNull(auth2);

            System.out.println("Testing if auth tokens are equal");

            assertEquals(a1.getUUID(), auth1.getUUID());
            assertEquals(a2.getUUID(), auth2.getUUID());

            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void clear() {
        String n1 = "John";
        String n2 = "Jacob";
        String n3 = "Jingle";
        String ID1 = "345";
        String ID2 = "678";
        String ID3 = "275";

        AuthToken a1 = new AuthToken(n1, ID1);
        AuthToken a2 = new AuthToken(n2, ID2);
        AuthToken a3 = new AuthToken(n3, ID3);

        try{
            db.openConnection();
            db.createTables();

            AuthTokenDAO aDAO = db.getAuth();
            aDAO.createAuthToken(a1);
            aDAO.createAuthToken(a2);
            aDAO.createAuthToken(a3);

            // get one auth token to make sure data base has objects
            AuthToken test1 = aDAO.getAuthToken(a1.getUUID());

            assertNotNull(test1);

            // clear data base

            aDAO.clear();

            AuthToken test2 = aDAO.getAuthToken(a1.getUUID());
            assertNull(test2);

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }


}