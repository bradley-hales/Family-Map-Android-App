package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import Models.AuthToken;
import Models.Event;
import Request.EventRequest;
import Response.EventResponse;

public class EventService {
    /**
     *  Service to get either a single event
     *  or all events for a specified person
     */


    public EventService(){

    }



    /**
     * @return single Event object
     */
    public EventResponse getEvent(EventRequest evR){
        /**
         * get the single event with the
         * specified id by calling
         * EventDAO
         */
        DataBase db = new DataBase();
        EventResponse evRSP = new EventResponse();
        try{
            db.openConnection();

            String evID = evR.getEventID();
            String auth = evR.getAuthToken();

            //check if authtoken is valid
            AuthTokenDAO authDAO = db.getAuth();
            AuthToken aTok = authDAO.getAuthToken(auth);
            if (aTok == null){
                evRSP.setErrorMessage("Invalid auth token");
                db.closeConnection(true);
                return evRSP;
            }
            String user = aTok.getUserName();

            // check for valid event ID and that event belongs to user
            EventDAO evDAO = db.getEvent();
            Event event = evDAO.getEvent(evID);
            if (event == null){
                evRSP.setErrorMessage("Invalid event ID");
                db.closeConnection(true);
                return evRSP;
            }
            if (!event.getDescendant().equals(user)){
                evRSP.setErrorMessage("Event does not belong to this user");
                db.closeConnection(true);
                return evRSP;
            }


            //close connection
            db.closeConnection(true);

            evRSP.setEvent(event);


        }catch (DataBaseException d){
            db.closeConnection(false);
            evRSP.setErrorMessage("Error while getting event " + evR.getEventID());
        }

        return evRSP;
    }

    /**
     * returns all events for a
     * user
     * @return array of events
     */
    public EventResponse getEvents(EventRequest evR){

        DataBase db = new DataBase();
        ArrayList<Event> evList = new ArrayList<>();
        EventResponse evRSP = new EventResponse();
        try{
            String auth = evR.getAuthToken();
            db.openConnection();

            //check if authtoken is valid
            AuthTokenDAO authDAO = db.getAuth();
            AuthToken aTok = authDAO.getAuthToken(auth);
            if (aTok == null){
                evRSP.setErrorMessage("Invalid auth token"); // if error return event response with one eventresponse
                db.closeConnection(true);
                return evRSP;
            }
            String user = aTok.getUserName();


            EventDAO evDAO = db.getEvent();
            evList = evDAO.getEvents(user);

            //close connection
            db.closeConnection(true);

            evRSP.setEventList(evList);


        }catch (DataBaseException d){
            db.closeConnection(false);
            evRSP.setErrorMessage("Error while getting events for  " + evR.getAuthToken());
        }

        return evRSP;
    }

    public String createEventID(){
        return UUID.randomUUID().toString();
    }


}
