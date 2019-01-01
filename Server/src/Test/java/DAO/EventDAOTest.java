package DAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import Models.Event;
import Models.User;

import static org.junit.Assert.*;

public class EventDAOTest {

    DataBase db;
    ArrayList<Event> events;

    @Before
    public void setUp() throws Exception {
        db = new DataBase();
        events = makeTestEvents();
    }

    @After
    public void tearDown() throws Exception {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();


            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
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

    @Test
    public void createEvent() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();

            for(Event e: events){
                eDAO.createEvent(e);
                Event test = eDAO.getEvent(e.getEventID());
                assertNotNull(test);
                assertEquals(e.getEventID(), test.getEventID());
                assertEquals(e.getDescendant(), test.getDescendant());
                assertEquals(e.getPerson(), test.getPerson());
                assertTrue(e.getLatitude() == test.getLatitude());
                assertTrue(e.getLongitude() == test.getLongitude());
                assertEquals(e.getCountry(), test.getCountry());
                assertEquals(e.getCity(), test.getCity());
                assertEquals(e.getEventType(), test.getEventType());
                assertEquals(e.getYear(), test.getYear());
            }


            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void getEvent() {
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();

            for(Event e: events){
                eDAO.createEvent(e);
            }

            Event ev1 = eDAO.getEvent(events.get(1).getEventID());
            assertNotNull(ev1);
            assertEquals(ev1.getEventID(), events.get(1).getEventID());

            Event ev2 = eDAO.getEvent(events.get(3).getEventID());
            assertNotNull(ev2);
            assertEquals(ev2.getEventID(), events.get(3).getEventID());

            // try to get event not in database
            Event ev3 = eDAO.getEvent("6789");
            assertNull(ev3);


            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }

    }

    @Test
    public void getEvents() {
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();

            for(Event e: events){
                eDAO.createEvent(e);
            }

            ArrayList<Event> ev1 = eDAO.getEvents("Joey");
            assertNotNull(ev1);
            assertEquals(ev1.size(), 2);

            ArrayList<Event> ev2 = eDAO.getEvents("Jessica");
            assertNotNull(ev2);
            assertEquals(ev2.size(), 1);

            ArrayList<Event> ev3 = eDAO.getEvents("Zach");
            assertNotNull(ev3);
            assertEquals(ev3.size(), 1);
//
//            // try to get events not in database
            ArrayList<Event> ev4 = eDAO.getEvents("Johnny");
            assertNull(ev4);


            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

    @Test
    public void deleteByUser() {

        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();

            for(Event e: events){
                eDAO.createEvent(e);
            }

            // delete events associated with username Joey
            eDAO.deleteByUser("Joey");
            Event ev1 = eDAO.getEvent(events.get(0).getEventID());
            assertNull(ev1);

            eDAO.deleteByUser("Jessica");
            Event ev2 = eDAO.getEvent(events.get(2).getEventID());
            assertNull(ev2);
//
            eDAO.deleteByUser("Zach");
            Event ev3 = eDAO.getEvent(events.get(3).getEventID());
            assertNull(ev3);


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
            EventDAO eDAO = db.getEvent();

            for(Event e: events){
                eDAO.createEvent(e);
            }

            // make sure there are events
            ArrayList<Event> ev1 = eDAO.getEvents("Joey");
            assertNotNull(ev1);
            assertEquals(ev1.size(), 2);

            // clear
            eDAO.clear();

            // try to get events for Joey
            ArrayList<Event> ev2 = eDAO.getEvents("Joey");
            assertNull(ev2);


            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
    }

}