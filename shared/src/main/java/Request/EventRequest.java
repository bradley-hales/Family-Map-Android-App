package Request;

import java.util.HashMap;

public class EventRequest {
    /**
     * EventRequest object takes in eventID (string of event to find)
     * and username (String, used to make sure authtoken is valid)
     */

    String eventID;
    String authToken;

    public EventRequest(String evID, String authTok){
        eventID = evID;
        authToken = authTok;
    }

    /**
     * If eventID not specified then set eventID
     * to null and we need to get all events
     * @param authTok username (string)
     */
    public EventRequest(String authTok){
        eventID = null;
        authToken = authTok;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
