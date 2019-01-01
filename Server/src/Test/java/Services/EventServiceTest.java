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
import Models.AuthToken;
import Models.Event;
import Request.EventRequest;
import Response.EventResponse;

import static org.junit.Assert.*;

public class EventServiceTest {
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
        String desc1 = "brad";
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
    public void getEvent() {
        // create auth token
        String u1 = "brad";
        String u1ID = "1234";
        AuthToken a1 = new AuthToken(u1, u1ID);
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();
            AuthTokenDAO aDAO = db.getAuth();

            aDAO.createAuthToken(a1);
            db.getConn().commit();
            AuthToken testA = aDAO.getAuthToken(a1.getUUID());
            assertNotNull(testA);
            System.out.println(testA.getUUID());
            assertEquals(a1.getUUID(), testA.getUUID());

            for(Event e: events){
                eDAO.createEvent(e);
                System.out.println(e.getEventID());
                Event test = eDAO.getEvent(e.getEventID());
                assertNotNull(test);
                assertEquals(e.getEventID(), test.getEventID());
            }
            db.getConn().commit();

            Event e1 = eDAO.getEvent(events.get(0).getEventID());
            assertNotNull(e1);
            assertEquals(e1.getEventID(), events.get(0).getEventID());

            String eventID = e1.getEventID();
            String authTOk = a1.getUUID();

            EventRequest request = new EventRequest(eventID, authTOk);

            EventService service = new EventService();
            EventResponse response = new EventResponse();

            response = service.getEvent(request);

            assertNotNull(response);
            Event testEv = response.getEvent();
            assertNotNull(testEv);

        db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
        catch (SQLException s){
            db.closeConnection(false);
            s.printStackTrace();
        }
    }

    @Test
    public void getEvents() {

        String u1 = "brad";
        String u1ID = "1234";
        AuthToken a1 = new AuthToken(u1, u1ID);
        try{
            db.openConnection();
            db.createTables();
            db.clearAll();

            EventDAO eDAO = db.getEvent();
            AuthTokenDAO aDAO = db.getAuth();

            aDAO.createAuthToken(a1);
            db.getConn().commit();
            AuthToken testA = aDAO.getAuthToken(a1.getUUID());
            System.out.println(testA.getUUID());
            assertNotNull(testA);
            assertEquals(a1.getUUID(), testA.getUUID());

            for(Event e: events){
                eDAO.createEvent(e);
                Event test = eDAO.getEvent(e.getEventID());
                assertNotNull(test);
                assertEquals(e.getEventID(), test.getEventID());
            }
            db.getConn().commit();

            EventRequest request = new EventRequest(testA.getUUID());

            EventService service = new EventService();
            EventResponse response = new EventResponse();

            response = service.getEvents(request);

            assertNotNull(response);
            ArrayList<Event> testEv = response.getEventList();
            assertNotNull(testEv);
            assertEquals(testEv.size(), 2);

            db.closeConnection(true);

        }catch (DataBaseException d){
            db.closeConnection(false);
            d.printStackTrace();
        }
        catch (SQLException s){
            db.closeConnection(false);
            s.printStackTrace();
        }
    }

}