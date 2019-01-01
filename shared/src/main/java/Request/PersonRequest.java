package Request;

import java.util.HashMap;

public class PersonRequest {
    /**
     * Create PersonRequest
     */

    String personID;
    String authToken;

    /**
     * Username included to check if
     * auth token is valid
     * @param pID String of person ID
     * @param auth String Username
     */
    public PersonRequest(String pID, String auth) {
        personID = pID;
        authToken = auth;
    }

    /**
     * If person ID not included then
     * request is to get all people
     * associated with the username
     * @param auth String username
     */
    public PersonRequest(String auth){
        personID = null;
        authToken = auth;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
