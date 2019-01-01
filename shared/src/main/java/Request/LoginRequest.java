package Request;

import java.util.HashMap;

public class LoginRequest {
    /**
     * Creates a LoginRequest object
     */

    String userName;
    String password;

    /**
     *
     * @param uName String, username
     * @param passW String, password
     */
    public LoginRequest(String uName, String passW){
        userName = uName;
        password = passW;
    }

    public LoginRequest(){}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
