package Models;

import java.util.UUID;

public class User {
    /**
     * create a user object with username (String), password (unique string),
     * email (string), first_name, last_name (String Names), gender (String M/F),
     * personID (references person table)
     */

    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;
    String personID;

    public User(String uname, String pword, String em,
                String fname, String lname, String gen, String pid){

        userName = uname;
        password = pword;
        email = em;
        firstName = fname;
        lastName = lname;
        gender = gen;
//        personID = UUID.randomUUID().toString();

        personID = pid;
    }

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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
