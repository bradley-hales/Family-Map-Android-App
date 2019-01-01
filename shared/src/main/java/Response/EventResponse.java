package Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Event;

public class EventResponse {
    /**
     * creates Event Response
     * returns success or error message
     */


    ArrayList<Event> eventList;
    Event event;
    String errorMessage = null;



    public EventResponse(){

    }

    /**
     *
     * @param message String error message
     * @return
     */
    public String errorMessage(String message){
        /**
         * returns error message
         */
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
