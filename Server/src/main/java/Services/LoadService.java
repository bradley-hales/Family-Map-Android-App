package Services;

import java.util.ArrayList;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Models.AuthToken;
import Models.Event;
import Request.LoadRequest;
import Models.Person;
import Models.User;
import Request.LoadRequest;
import Response.LoadResponse;
import Response.LoginResponse;

public class LoadService {
    /**
     * clears all data from DB
     * Loads User, Person and event
     * info into the database
     */



    public LoadService(){

    }

    public LoadResponse load(LoadRequest lr){
        /**
         * clears all data using clear function in database,
         * creates users from the arrray of given users
         * by creating RegisterRequest and using RegistrationService,
         * loads event and person data into the database
         * by creating the objects using the person and event
         * services
         */

        DataBase db = new DataBase();
        LoadResponse lRSP = new LoadResponse();
        try{
            db.openConnection();

            // clear all data
            db.clearAll();

            // get arrays of user, person, events
            ArrayList<Person> people = lr.getPersons();
            ArrayList<Event> events = lr.getEvents();
            ArrayList<User> users = lr.getUsers();

            if(people == null || events == null || users == null){
                db.closeConnection(false);
                lRSP.setMessage("Missing array or values");
                return lRSP;
            }

            // load data into database
            PersonDAO pDAO = db.getPerson();
            for (Person p: people){
                pDAO.createPerson(p);
            }
            EventDAO eDAO = db.getEvent();
            for (Event e: events){
                eDAO.createEvent(e);
            }
            UserDAO uDAO = db.getUser();
            for (User u: users){
                uDAO.createUser(u);
            }

            // success message
            lRSP.setMessage("Successfully add " + String.valueOf(people.size()) +
                            " People " + String.valueOf(events.size()) + " events " + String.valueOf(users.size())
                            + " users to the database");

            //close connection
            db.closeConnection(true);


        }catch (DataBaseException d){
            db.closeConnection(false);
            lRSP.setMessage("Error while Loading " + d.getMessage());
        }

        return lRSP;
    }



}
