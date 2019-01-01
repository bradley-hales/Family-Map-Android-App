package Services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.Event;
import Models.Person;
import Models.User;
import Request.LoadRequest;
import Response.LoadResponse;

import static org.junit.Assert.*;

public class LoadServiceTest {
    DataBase db;
    ArrayList<Person> people;
    ArrayList<Event> events;
    ArrayList<User> users;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
        people = createPeople();
        events = makeTestEvents();
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

    @Test
    public void load() {

        try{
            db.openConnection();
            db.createTables();

            LoadRequest request = new LoadRequest(events, people, users);
            db.closeConnection(true);
            LoadResponse response;

            LoadService service = new LoadService();

            response = service.load(request);

            assertEquals(response.getMessage(), "Successfully add 4 People 4 events 3 users to the database");

            // bad data
            db.openConnection();
            LoadRequest r2 = new LoadRequest(events, people, null);
            db.closeConnection(true);
            LoadResponse resp2;
            LoadService serv2 = new LoadService();
            resp2 = serv2.load(r2);
            assertEquals(resp2.getMessage(), "Missing array or values");

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

    public ArrayList<Person> createPeople(){
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();
        String pID4 = UUID.randomUUID().toString();
        String desc = "Brody";
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

    private ArrayList<Event> makeTestEvents(){
        String eID1 = UUID.randomUUID().toString();
        String eID2 = UUID.randomUUID().toString();
        String eID3 = UUID.randomUUID().toString();
        String eID4 = UUID.randomUUID().toString();
        String desc1 = "Joey";
        String desc2 = "Jessica";
        String desc3 = "Zach";
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();
        String pID4 = UUID.randomUUID().toString();
        String lat1 = "345.7889";
        String lat2 = "567.6878";
        String lat3 = "23.456";
        String lat4 = "1122.455";
        String lng1 = "900.00";
        String lng2 = "224.456";
        String lng3 = "876.456";
        String lng4 = "988.098";
        String cnty = "USA";
        String cty1 = "Provo";
        String cty2 = "Salt Lake";
        String cty3 = "Denver";
        String cty4 = "San Diego";
        String ev1 = "Baptism";
        String ev2 = "Marriage";
        String ev3 = "Birth";
        String ev4 = "Death";
        String y1 = "1999";
        String y2 = "2000";
        String y3 = "2010";
        String y4 = "1960";

        ArrayList<Event> events = new ArrayList<>();

        events.add(new Event(eID1, desc1, pID1, lat1, lng1, cnty, cty1, ev1, y1));
        events.add(new Event(eID2, desc1, pID2, lat2, lng2, cnty, cty2, ev2, y2));
        events.add(new Event(eID3, desc2, pID3, lat3, lng3, cnty, cty3, ev3, y3));
        events.add(new Event(eID4, desc3, pID4, lat4, lng4, cnty, cty4, ev4, y4));

        return events;

    }
}