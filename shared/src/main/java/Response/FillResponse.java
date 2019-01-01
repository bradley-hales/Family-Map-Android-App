package Response;

public class FillResponse {
    /**
     * returns success or error message
     */
    String message;


    public FillResponse(){
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
