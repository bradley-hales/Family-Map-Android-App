package Request;

import java.util.HashMap;

public class UserRequest {
    /**
     * Find a user in the database
     */

    String username;

    /**
     *
     * @param uName String username to find
     */
    public UserRequest(String uName){
        username = uName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
