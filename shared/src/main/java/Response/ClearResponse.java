package Response;


public class ClearResponse {
    /**
     * returns a success message
     * or error message
     */
    String message;

    public ClearResponse(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
