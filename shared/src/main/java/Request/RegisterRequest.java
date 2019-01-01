package Request;

import java.util.HashMap;
import java.util.UUID;

public class RegisterRequest {
    /**
     * Register a user
     */
    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;


    /**
     *
     * @param uname username of registree
     * @param pword String password
     * @param em String email
     * @param fname String first name
     * @param lname String last name
     * @param gen sttring gender (M/F)
     */
    public RegisterRequest(String uname, String pword, String em, String fname,
                           String lname, String gen){
        userName = uname;
        password = pword;
        email = em;
        firstName = fname;
        lastName = lname;
        gender = gen;


    }
    public RegisterRequest(){}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
