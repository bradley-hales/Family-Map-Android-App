package com.example.bradl.familymapserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Request.RegisterRequest;
import Response.RegisterResponse;

import static org.junit.Assert.*;

public class ModelTest {
    Model model;
    ArrayList<Person> people;
    ArrayList<Event> events;
    User u;
    ServerProxy server;
    String authToken;
    String userID;
    String birthID;

    @Before
    public void setUp(){
        model = Model.getInstance();
        people = createPeople();
        events = makeTestEvents();

//        String u1 = "Magnus";
//        String pw1 = "456";
//        String e1 = "a@gmail.com";
//        String f1 = "Tom";
//        String l1 = "Thompson";
//        String m = "m";
//        String pID1 = UUID.randomUUID().toString();
//        u = new User(u1, pw1, e1, f1, l1, m, pID1);
//
//        // initialize server
//        server = new ServerProxy("192.168.1.194", "8080");

    }
    @After
    public void tearDown(){

    }


    @Test
    public void fillPeople() {
        // fill people map
        int pArrayLength = people.size();
        model.fillPeople(people);

        HashMap<String, Person> pArray = model.getPeople();


        assertEquals(pArray.size(), pArrayLength);

        // make sure certain person is in map
        Person testP = people.get(3);
        String pID = testP.getPersonID();

        Person psn = pArray.get(pID);

        assertNotNull(psn);

    }

    @Test
    public void fillEvents() {
        int eArrayLength = events.size();
        // fill events
        model.personID = userID;
        model.fillPeople(people);
        model.fillEvents(events);
        HashMap<String, Event> eArray = model.getEvents();


        assertEquals(eArray.size(), eArrayLength);

        // make sure certain event is in map
        Event testEvent = events.get(2);
        String eID = testEvent.getEventID();

        Event evt = eArray.get(eID);

        assertNotNull(evt);

    }
    @Test
    public void filterEvents(){
        model.personID = userID;
        model.fillPeople(people);
        model.fillEvents(events);
        model.fillDictionairies();
        HashMap<String, Event> evBefore = model.getFilteredEvents();

        assertNotNull(evBefore.get(birthID));

        //apply filter
        model.applyFilterToDictionaries("birth", false);
        HashMap<String, Event> evAfter = model.getFilteredEvents();

        // no longer in events
        assertNull(evAfter.get(birthID));

    }
    @Test
    public void sortEvents(){
        model.personID = userID;
        model.fillPeople(people);
        model.fillEvents(events);
        model.fillDictionairies();

        ArrayList<Event> events = model.getPersonEvents(userID);
        assertNotNull(events);

        assertEquals(events.get(0).getEventType(), "birth");
        assertEquals(events.get(events.size()-1).getEventType(), "death");
    }

    @Test
    public void findPeople(){
        model.personID = userID;
        model.fillPeople(people);
        model.fillEvents(events);
        model.fillDictionairies();

        // get the user
        Person p = model.getPerson(userID);

        assertEquals(p.getPersonID(), userID);

        // someone not in data base
        Person p2 = model.getPerson("99888");
        assertNull(p2);
    }
    @Test
    public void findEvent(){
        model.personID = userID;
        model.fillPeople(people);
        model.fillEvents(events);
        model.fillDictionairies();

        // get birth event
        Event e = model.getEvent(birthID);
        assertEquals(e.getEventID(), birthID);

        // get event not in database
        Event e2 = model.getEvent("9003");
        assertNull(e2);
    }

    @Test
    public void getName() {
        // fill model with people
        model.fillPeople(people);

        // get a random person
        Person testP = people.get(2);
        // set the user in model
        model.setPersonID(testP.getPersonID());
        // get test name
        String testName = testP.getFirstName() + " " + testP.getLastName();

        String name = model.getName();

        assertEquals(testName, name);

        model.getPeople().clear();

    }

    public ArrayList<Person> createPeople(){
        String pID1 = UUID.randomUUID().toString();
        String pID2 = UUID.randomUUID().toString();
        String pID3 = UUID.randomUUID().toString();
        String pID4 = UUID.randomUUID().toString();
        String desc = pID1;
        userID = pID1;
        String n1 = "Brad";
        String n2 = "Joe";
        String n3 = "Aaron";
        String n4 = "Jess";
        String last = "Williams";
        String m = "m";
        String f = "f";
        // make mother and father
        String mID = UUID.randomUUID().toString();
        String fID = UUID.randomUUID().toString();
        String fName = "Jack";
        String mName = "Jill";
        String lName = "Hill";

        ArrayList<Person> list = new ArrayList<>();

        list.add(new Person(pID1, desc, n1, last, m, fID, mID, null));
        list.add(new Person(pID2, desc, n2, last, m, fID, mID, null));
        list.add(new Person(pID3, desc, n3, last, m, fID, mID, null));
        list.add(new Person(pID4, desc, n4, last, f, fID, mID, null));
        list.add(new Person(fID, desc, fName, lName, m, null, null, mID));
        list.add(new Person(mID, desc, mName, lName, f, null, null, fID));

        return list;

    }

    private ArrayList<Event> makeTestEvents(){
        String eID1 = UUID.randomUUID().toString();
        String eID2 = UUID.randomUUID().toString();
        String eID3 = UUID.randomUUID().toString();
        birthID = eID1;
        String eID4 = UUID.randomUUID().toString();
        String desc1 = "brad";
        String desc2 = "Jessica";
        String desc3 = "Zach";
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
        String ev2 = "baptism";
        String ev3 = "marriage";
        String ev1 = "birth";
        String ev4 = "death";
        String y1 = "1999";
        String y2 = "2000";
        String y3 = "2010";
        String y4 = "2060";

        ArrayList<Event> events = new ArrayList<>();

        events.add(new Event(eID1, userID, userID, lat1, lng1, cnty, cty1, ev1, y1));
        events.add(new Event(eID2, userID, userID, lat2, lng2, cnty, cty2, ev2, y2));
        events.add(new Event(eID3, userID, userID, lat3, lng3, cnty, cty3, ev3, y3));
        events.add(new Event(eID4, userID, userID, lat4, lng4, cnty, cty4, ev4, y4));

        return events;

    }

//    public void register() {
//        String uName = u.getUserName();
//        String passW = u.getPassword();
//        String f = u.getFirstName();
//        String l = u.getLastName();
//        String email = u.getEmail();
//        String gender = u.getGender();
//
//        // create register request
//        RegisterRequest request = new RegisterRequest(uName, passW, email, f, l, gender);
//
//        RegisterResponse response =  server.Register(request);
//
//        authToken = response.getAuth().getUUID();
//    }
}