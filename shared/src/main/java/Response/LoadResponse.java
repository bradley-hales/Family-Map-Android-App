package Response;

public class LoadResponse {
    /**
     * returns success or error message
     */

    String message;

    public LoadResponse(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
