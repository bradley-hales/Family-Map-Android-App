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
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Request.EventRequest;
import Request.PersonRequest;
import Response.EventResponse;
import Response.PersonResponse;

import static org.junit.Assert.*;

public class PersonServiceTest {

    DataBase db;
    ArrayList<Person> people;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
        people = createPeople();
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
    public void getPerson() {

        // create auth token
        String u1 = "brad";
        String u1ID = "1234";
        AuthToken a1 = new AuthToken(u1, u1ID);

        try{
            db.openConnection();
            db.createTables();

            PersonDAO pDAO = db.getPerson();
            AuthTokenDAO aDAO = db.getAuth();

            aDAO.createAuthToken(a1);
            db.closeConnection(true);
            db.openConnection();
            AuthToken testA = aDAO.getAuthToken(a1.getUUID());
            assertNotNull(testA);
//            System.out.println(testA.getUUID());
            assertEquals(a1.getUUID(), testA.getUUID());

            for(Person p: people){
                pDAO.createPerson(p);
//                System.out.println(p.getPersonID());
                Person test = pDAO.getPerson(p.getPersonID());
                assertNotNull(test);
                assertEquals(p.getPersonID(), test.getPersonID());
            }
            db.closeConnection(true);
            db.openConnection();

            String pID = people.get(0).getPersonID();
//            System.out.println("person id: " + people.get(0).getPersonID());
            String authTOk = a1.getUUID();

            PersonRequest request = new PersonRequest(pID, authTOk);

            PersonService service = new PersonService();
            PersonResponse response;

            response = service.getPerson(request);

            assertNotNull(response);
            Person testP = response.getPerson();
            assertNotNull(testP);
            assertEquals(testP.getPersonID(), people.get(0).getPersonID());

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

    public ArrayList<Person> createPeople(){
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();
        String pID4 = UUID.randomUUID().toString();
        String desc = "brad";
        String n1 = "Brad";
        String n2 = "Joe";
        String n3 = "Aaron";
        String n4 = "Jess";
        String last = "Williams";
        String m = "m";
        String f = "f";
        String father = "Jack";
        String mother = "Jill";

        ArrayList<Person> list = new ArrayList<>();

        list.add(new Person(pID1, desc, n1, last, m, father, mother, null));
        list.add(new Person(pID2, desc, n2, last, m, father, mother, null));
        list.add(new Person(pID3, desc, n3, last, m, father, mother, null));
        list.add(new Person(pID4, desc, n4, last, f, father, mother, null));

        return list;

    }

    @Test
    public void getPeople() {
        // create auth token
        String u1 = "brad";
        String u1ID = "1234";
        AuthToken a1 = new AuthToken(u1, u1ID);

        try{
            db.openConnection();
            db.createTables();

            PersonDAO pDAO = db.getPerson();
            AuthTokenDAO aDAO = db.getAuth();

            aDAO.createAuthToken(a1);
            db.closeConnection(true);
            db.openConnection();
            AuthToken testA = aDAO.getAuthToken(a1.getUUID());
            assertNotNull(testA);
            assertEquals(a1.getUUID(), testA.getUUID());

            for(Person p: people){
                pDAO.createPerson(p);
                Person test = pDAO.getPerson(p.getPersonID());
                assertNotNull(test);
                assertEquals(p.getPersonID(), test.getPersonID());
            }
            db.closeConnection(true);
            db.openConnection();

            String authTOk = a1.getUUID();

            PersonRequest request = new PersonRequest(authTOk);

            PersonService service = new PersonService();
            PersonResponse response;

            response = service.getPeople(request);

            assertNotNull(response);
            ArrayList<Person> testP = response.getPersonArray();
            assertNotNull(testP);
            assertEquals(testP.size(), 4);

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }
}