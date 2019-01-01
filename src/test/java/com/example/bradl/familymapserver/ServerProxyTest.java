package com.example.bradl.familymapserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import Models.AuthToken;
import Models.Event;
import Models.Person;
import Models.User;
import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Response.EventResponse;
import Response.LoginResponse;
import Response.PersonResponse;
import Response.RegisterResponse;

import static org.junit.Assert.*;

public class ServerProxyTest {
    User u;
    ServerProxy server;

    @Before
    public void setUp() {
        // create a user
        String u1 = "Test";
        String pw1 = "456";
        String e1 = "a@gmail.com";
        String f1 = "Tom";
        String l1 = "Thompson";
        String m = "m";
        String pID1 = UUID.randomUUID().toString();
        u = new User(u1, pw1, e1, f1, l1, m, pID1);

        // initialize server
        server = new ServerProxy("192.168.1.194", "8080");


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register() {
        String uName = u.getUserName();
        String passW = u.getPassword();
        String f = u.getFirstName();
        String l = u.getLastName();
        String email = u.getEmail();
        String gender = u.getGender();

        // create register request
        RegisterRequest request = new RegisterRequest(uName, passW, email, f, l, gender);

        RegisterResponse response =  server.Register(request);

        AuthToken auth = response.getAuth();
        String message = response.getMessage();

        assertNotNull(auth);

        assertEquals(auth.getUserName(), "Test");

    }

    @Test
    public void login() {
        String uName = u.getUserName();
        String passW = u.getPassword();

        LoginRequest loginRequest = new LoginRequest(uName, passW);
        // user that is not in data base
        LoginRequest badRequest = new LoginRequest("Jeff", "8977");

        LoginResponse response = server.Login(loginRequest);
        AuthToken auth = response.getAuth();
        String message = response.getMessage();

        assertNotNull(auth);

        assertEquals(auth.getUserName(), "Test");

        // ask for user not in database
        LoginResponse badResp = server.Login(badRequest);

        String msg = badResp.getMessage();

        assertEquals(msg, "Invalid username");
    }
    @Test
    public void getPeople(){
        // login to get authToken
        String uName = u.getUserName();
        String passW = u.getPassword();

        LoginRequest loginRequest = new LoginRequest(uName, passW);

        LoginResponse response = server.Login(loginRequest);
        AuthToken auth = response.getAuth();

        PersonRequest pR = new PersonRequest(auth.getUUID());
        PersonResponse pResponse = server.getPeople(pR);

        ArrayList<Person> people = pResponse.getPersonArray();

        assertNotNull(people);

        // bad auth token
        PersonRequest pR2 = new PersonRequest("393993l");
        PersonResponse pResponse2 = server.getPeople(pR2);

        ArrayList<Person> people2 = pResponse2.getPersonArray();

        assertNull(people2);

    }
    @Test
    public void getEvents(){
        // login to get authToken
        String uName = u.getUserName();
        String passW = u.getPassword();

        LoginRequest loginRequest = new LoginRequest(uName, passW);

        LoginResponse response = server.Login(loginRequest);
        AuthToken auth = response.getAuth();

        EventRequest eR = new EventRequest(auth.getUUID());
        EventResponse eResponse = server.getEvents(eR);

        ArrayList<Event> events = eResponse.getEventList();

        assertNotNull(events);

        // bad auth token
        EventRequest eR2 = new EventRequest("y9050");
        EventResponse eResponse2 = server.getEvents(eR2);

        ArrayList<Event> events2 = eResponse2.getEventList();

        assertNull(events2);

    }



}