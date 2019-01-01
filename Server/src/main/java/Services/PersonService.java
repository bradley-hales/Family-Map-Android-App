package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import Models.AuthToken;
import Models.Event;
import Models.Person;
import Request.PersonRequest;
import Response.EventResponse;
import Response.PersonResponse;

public class PersonService {
    /**
     * Create or get a single person
     * or multiple people
     */


    public PersonService(){

    }

    /**
     * finds the given person in the database
     * and returns them if found
     * @param pr PersonRequest object
     * @return Person object
     */
    public PersonResponse getPerson(PersonRequest pr){

        DataBase db = new DataBase();
        PersonResponse pRSP = new PersonResponse(); // can I add a response
        try{
            db.openConnection();
            String pID = pr.getPersonID();
            String auth = pr.getAuthToken();

            //check if authtoken is valid
            AuthTokenDAO authDAO = db.getAuth();
            AuthToken aTok = authDAO.getAuthToken(auth);
            if (aTok == null){
                pRSP.setMessage("Invalid auth token");
                db.closeConnection(true);
                return pRSP;
            }
            String user = aTok.getUserName();

            // check for valid person ID and that person belongs to user
            PersonDAO pDAO = db.getPerson();
            Person person = pDAO.getPerson(pID);
            if (person == null){
                pRSP.setMessage("Invalid person ID");
                db.closeConnection(true);
                return pRSP;
            }
            if (!person.getDescendant().equals(user)){
                pRSP.setMessage("Person does not belong to this user");
                db.closeConnection(true);
                return pRSP;
            }




            //close connection
            db.closeConnection(true);

            pRSP.setPerson(person);

        }catch (DataBaseException d){
            db.closeConnection(false);
            pRSP.setMessage("Error while getting person " + pr.getPersonID()); // is this where I send response?
        }

        return pRSP;
    }


    public String createPersonID(){
        return UUID.randomUUID().toString();
    }

    /**
     * get all family members of the current user
     * @param pr PersonRequest object
     * @return List of people associated with user
     */
    public PersonResponse getPeople(PersonRequest pr){

        DataBase db = new DataBase();
        ArrayList<Person> personList = new ArrayList<>();
        PersonResponse pRSP = new PersonResponse();
        try{
            db.openConnection();
            String auth = pr.getAuthToken();

            //check if authtoken is valid
            AuthTokenDAO authDAO = db.getAuth();
            AuthToken aTok = authDAO.getAuthToken(auth);
            if (aTok == null){
                pRSP.setMessage("Invalid auth token");
                db.closeConnection(true);
                return pRSP;
            }
            String user = aTok.getUserName();


            PersonDAO pDAO = db.getPerson();
            personList = pDAO.getAllPeople(user);

            //close connection
            db.closeConnection(true);

            pRSP.setPersonArray(personList);


        }catch (DataBaseException d){
            db.closeConnection(false);
            pRSP.setMessage("Error while getting people");
        }

        return pRSP;
    }

}
