package Models;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AuthToken {
    /**
     * create auth Token with
     * String userName, a string which is
     * the UUID, and a TimeStamp time stamp
     */

    String authToken;
    String userName;
//    String timeStamp;
    String personID;

    public AuthToken(String username, String pID){
        userName = username;
        // get today's date
//        Date date = new Date();
        // make string format for date
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//        timeStamp = df.format(date);
        authToken = UUID.randomUUID().toString();
        personID = pID;
    }

    public AuthToken(){

    }

    public String getUUID() {
        return authToken;
    }

    public void setUUID(String uuid) {
        this.authToken = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    public String getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(String time) {
//        this.timeStamp = time;
//    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
