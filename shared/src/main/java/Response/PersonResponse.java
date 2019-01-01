package Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Person;

public class PersonResponse {
    /**
     * returns success or error message
     */


    ArrayList<Person> personArray;
    Person person;
    String message;


    public PersonResponse(){

    }

    /**
     * @return Person Response object with either one persons info
     * or an array of People info
     */
    public ArrayList<Person> getPersonArray() {
        return personArray;
    }

    public void setPersonArray(ArrayList<Person> personArray) {
        this.personArray = personArray;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param message String, error message to display
     * @return String error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
