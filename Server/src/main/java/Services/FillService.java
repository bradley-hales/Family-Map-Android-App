package Services;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.corba.se.impl.interceptors.PICurrent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
import Request.FillRequest;
import Response.EventResponse;
import Response.FillResponse;


public class FillService {
    /**
     * Populates fake data to fill in database
     */

    // file paths to json files
    String malePath = "C:/Users/bradl/Documents/CS_240/FamilyMapServer/Server/json/mnames.json";
    String femalePath = "C:/Users/bradl/Documents/CS_240/FamilyMapServer/Server/json/fnames.json";
    String locationPath = "C:/Users/bradl/Documents/CS_240/FamilyMapServer/Server/json/locations.json";
    String lastNamePath = "C:/Users/bradl/Documents/CS_240/FamilyMapServer/Server/json/snames.json";

    // lastname, malename, femalename, location objects
    Locations locations;
    FemaleNames female;
    MaleNames male;
    LastName lastName;


    public FillService(){

    }




    public FillResponse fill(FillRequest fr){
        /**
         * populates servers database with generated data
         * by checking to see if Username already in database,
         * delete any data already associated with the given
         * user by calling UserDAO methods
         */
        DataBase db = new DataBase();
        FillResponse fRSP = new FillResponse();
        try{
            db.openConnection();
            String userN = fr.getUsername();
            int gen;

            // make sure generations is an int object
            try{
                gen = Integer.valueOf(fr.getGenerations());
            }catch(NumberFormatException e){
                fRSP.setMessage("generations has to be an integer");
                db.closeConnection(false);
                return fRSP;
            }


            // make sure generations is non-negative integer
            if ( gen < 0){
                fRSP.setMessage("generations cant be negative");
                db.closeConnection(true);
                return fRSP;
            }

            //check if user is registered
            UserDAO userDAO = db.getUser();
            User user = userDAO.getUser(userN);

            if (user == null){
                fRSP.setMessage("user is not registered");
                db.closeConnection(true);
                return fRSP;
            }

            // clear any existing user info
            PersonDAO personDAO = db.getPerson();
            EventDAO eventDAO = db.getEvent();

            personDAO.deleteByUser(userN);
            eventDAO.deleteByUser(userN);

            // initialize locations, male/female names
            readFiles();

            // create generations of data and create user's person
            Person userPerson = new Person(user.getPersonID(), userN, user.getFirstName(),
                                            user.getLastName(), user.getGender());

            int startYear = ThreadLocalRandom.current().nextInt(1990, 2005);
            ArrayList<Person> parents = generateFatherMother(userN, gen, db, startYear - 20);

            Person mother = parents.get(1);
            Person father = parents.get(0);

            userPerson.setFather(father.getPersonID());
            userPerson.setMother(mother.getPersonID());
            personDAO.createPerson(userPerson);

            // create user's person event
            ArrayList<Event> userEvents = createEvent(startYear, user.getPersonID(), userN);

            for(Event e: userEvents){
                eventDAO.createEvent(e);
            }

            // success, create response
            int numPeople = (int)(Math.pow(2.0,(double)(gen+1))) - 1;
            String numPpl = String.valueOf(numPeople);
            String numEvents = String.valueOf(numPeople * 4);

            fRSP.setMessage("Successfully add " + numPpl + " people " + numEvents + " events");

            //close connection
            db.closeConnection(true);



        }catch (DataBaseException d){
            db.closeConnection(false);
            fRSP.setMessage("Error while filling the database " + d.getMessage());
        }

        return fRSP;



    }

    private ArrayList<Person> generateFatherMother(String username, int genLeft, DataBase db, int year){

        // generate male person
        String mname = male.randomMaleName();
        String lname = lastName.randomLastName();
        String mPID = UUID.randomUUID().toString();

        Person father = new Person(mPID, username, mname, lname, "m");


        // generate female person
        String fname = female.randomFemaleName();
        String fPID = UUID.randomUUID().toString();

        Person mother = new Person(fPID, username, fname, lname, "f");

        father.setSpouse(mother.getPersonID());
        mother.setSpouse(father.getPersonID());



        if (genLeft > 1){ // was  > 0
            ArrayList<Person> nextParents1 = generateFatherMother(username, genLeft - 1, db, year - 20);
            ArrayList<Person> nextParents2 = generateFatherMother(username, genLeft - 1, db, year - 20);

            Person nFather1 = nextParents1.get(0);
            Person nMother1 = nextParents1.get(1);
            Person nFather2 = nextParents2.get(0);
            Person nMother2 = nextParents2.get(1);

            father.setFather(nFather1.getPersonID());
            father.setMother(nMother1.getPersonID());
            mother.setMother(nMother2.getPersonID());
            mother.setFather(nFather2.getPersonID());
        }

        // put father and mother in database
        PersonDAO pDAO = db.getPerson();
        EventDAO eDAO = db.getEvent();

        try {
            pDAO.createPerson(father);
            pDAO.createPerson(mother);

            // get list of events for father and mother
            ArrayList<Event> eventList = createParentsEvents(username, mother.getPersonID(), father.getPersonID(), year);

            // add event list to data base
            for(Event e: eventList){
                eDAO.createEvent(e);
            }

        }catch (DataBaseException d){
            d.printStackTrace();
        }


        ArrayList<Person> personList = new ArrayList<>();
        personList.add(father);
        personList.add(mother);

        return personList;


    }

    private ArrayList<Event> createParentsEvents(String user, String mPID, String fPID, int year){

        // birth for mother and father
        String eType = "Birth";
        String fBID = UUID.randomUUID().toString();
        String mBID = UUID.randomUUID().toString();

        Location floc = locations.randomLocation();
        Location mloc = locations.randomLocation();

        String fcntry = floc.getCountry();
        String fcity = floc.getCity();
        String flat = String.valueOf(floc.getLatitude());
        String flng = String.valueOf(floc.getLongitude());
        String mcntry = mloc.getCountry();
        String mcity = mloc.getCity(); // was floc
        String mlat = String.valueOf(mloc.getLatitude()); // was floc
        String mlng = String.valueOf(mloc.getLongitude()); // was floc
        int fBYR = ThreadLocalRandom.current().nextInt(year, year + 4);
        int mBYR = ThreadLocalRandom.current().nextInt(year, year + 4);

        Event fBirth = new Event(fBID, user, fPID, flat,
                flng, fcntry, fcity, eType, String.valueOf(fBYR));
        Event mBirth = new Event(mBID, user, mPID, mlat,
                mlng, mcntry, mcity, eType, String.valueOf(mBYR));

        // baptism
        String fBPID = UUID.randomUUID().toString();
        String mBPID = UUID.randomUUID().toString();

        Event fBaptism = new Event(fBPID, user, fPID, flat, flng, fcntry, fcity, "Baptism",
                                  String.valueOf(fBYR + 8)); // was mcntry
        Event mBaptism = new Event(mBPID, user, mPID, mlat, mlng, mcntry, mcity, "Baptism",
                String.valueOf(mBYR + 8));

        // marriage
        Location mMloc = locations.randomLocation();
        String mID = UUID.randomUUID().toString();
        String fID = UUID.randomUUID().toString();
        int mrgYR = ThreadLocalRandom.current().nextInt(fBYR + 20, fBYR + 26);
        Event fMarriage = new Event(fID, user, fPID, String.valueOf(mMloc.getLatitude()),
                                   String.valueOf(mMloc.getLongitude()), mMloc.getCountry(),
                                   mMloc.getCity(), "Marriage", String.valueOf(mrgYR)); // was mloc
        Event mMarriage = new Event(mID, user, mPID, String.valueOf(mMloc.getLatitude()),
                String.valueOf(mMloc.getLongitude()), mMloc.getCountry(),
                mMloc.getCity(), "Marriage", String.valueOf(mrgYR)); // was mloc

        // death
        Location deathLoc = locations.randomLocation();
        String mDID = UUID.randomUUID().toString();
        String fDID = UUID.randomUUID().toString();
        int mDYR = ThreadLocalRandom.current().nextInt(mBYR + 85, mBYR + 90);
        int fDYR = ThreadLocalRandom.current().nextInt(fBYR + 85, fBYR + 90);
        Event mDeath = new Event(mDID, user, mPID, String.valueOf(deathLoc.getLatitude()),
                String.valueOf(deathLoc.getLongitude()), deathLoc.getCountry(),
                deathLoc.getCity(), "Death", String.valueOf(mDYR));
        Event fDeath = new Event(fDID, user, fPID, String.valueOf(deathLoc.getLatitude()),
                String.valueOf(deathLoc.getLongitude()), deathLoc.getCountry(),
                deathLoc.getCity(), "Death", String.valueOf(fDYR));

        ArrayList<Event> events = new ArrayList<>();
        events.add(fBirth);
        events.add(fBaptism);
        events.add(fMarriage);
        events.add(fDeath);
        events.add(mBirth);
        events.add(mBaptism);
        events.add(mMarriage);
        events.add(mDeath);

        return events;

    }

    private ArrayList<Event> createEvent(int year, String pID, String userN){

        // birth
        Location loc = locations.randomLocation();
        String bID = UUID.randomUUID().toString();
        String cntry = loc.getCountry();
        String city = loc.getCity();
        String lat = String.valueOf(loc.getLatitude());
        String lng = String.valueOf(loc.getLongitude());

        Event birth = new Event(bID, userN, pID, lat,
                lng, cntry, city, "Birth", String.valueOf(year));

        // baptism
        String bpID = UUID.randomUUID().toString();
        Event baptism = new Event(bpID, userN, pID, lat, lng, cntry, city, "Baptism",
                String.valueOf(year + 8));



        // death
        Location deathLoc = locations.randomLocation();
        String dID = UUID.randomUUID().toString();
        int dYR = ThreadLocalRandom.current().nextInt(year + 85, year + 90);

        Event death = new Event(dID, userN, pID, String.valueOf(deathLoc.getLatitude()),
                String.valueOf(deathLoc.getLongitude()), deathLoc.getCountry(),
                deathLoc.getCity(), "Death", String.valueOf(dYR));

        ArrayList<Event> events = new ArrayList<>();
        events.add(birth);
        events.add(baptism);
        events.add(death);

        return events;


    }

    private void readFiles(){

        try{
            JsonReader locationReader = new JsonReader(new FileReader(locationPath));
            JsonReader fNamesReader = new JsonReader(new FileReader(femalePath));
            JsonReader mNamesReader = new JsonReader(new FileReader(malePath));
            JsonReader lastNameReader = new JsonReader(new FileReader(lastNamePath));

            Gson gson = new Gson();


            locations = gson.fromJson(locationReader, Locations.class);
            female = gson.fromJson(fNamesReader, FemaleNames.class);
            male = gson.fromJson(mNamesReader, MaleNames.class);
            lastName = gson.fromJson(lastNameReader, LastName.class);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }


    public class Location {
        String country;
        String city;
        double latitude;
        double longitude;

        public Location(){

        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public class Locations{

        ArrayList<Location> data;

        public Locations(){}

        public ArrayList<Location> getData() {
            return data;
        }

        public Location randomLocation(){
            Random rand = new Random();

            return data.get(rand.nextInt(data.size()));
        }
    }

    public class LastName {

        ArrayList<String> data;

        public LastName(){

        }

        public String randomLastName(){
            Random rand = new Random();

            return data.get(rand.nextInt(data.size()));
        }

        public ArrayList<String> getLastNames() {
            return data;
        }
    }

    public class MaleNames {

        ArrayList<String> data;

        public MaleNames(){}

        public String randomMaleName(){
            Random rand = new Random();

            return data.get(rand.nextInt(data.size()));
        }

        public ArrayList<String> getMaleNames() {
            return data;
        }
    }

    public class FemaleNames {

        ArrayList<String> data;

        public FemaleNames(){}

        public String randomFemaleName(){
            Random rand = new Random();

            return data.get(rand.nextInt(data.size()));
        }

        public ArrayList<String> getFemaleNames() {
            return data;
        }
    }

//    public String getUsername() {
//        return Username;
//    }
//
//    public void setUsername(String username) {
//        Username = username;
//    }
//
//    public Integer getGenerations() {
//        return generations;
//    }
//
//    public void setGenerations(Integer generations) {
//        this.generations = generations;
//    }

}
