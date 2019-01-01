package Response;

import Models.AuthToken;

public class LoginResponse {
    /**
     * returns success or error message
     * for logging in
     */

    AuthToken authToken;
    String message;

    public LoginResponse(){}


    public AuthToken getAuth() {
        return authToken;
    }

    public void setAuth(AuthToken auth) {
        this.authToken = auth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
