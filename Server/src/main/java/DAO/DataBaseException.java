package DAO;

public class DataBaseException extends Exception {
    String message = null;

    public DataBaseException(String error){
        message = error;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
