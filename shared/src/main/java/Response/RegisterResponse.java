package Response;

import Models.AuthToken;

public class RegisterResponse {
    /**
     * returns success or error message
     */
    AuthToken authToken;
    String message;



    public RegisterResponse(){
    }

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
