package Request;

import java.util.ArrayList;

import Models.Event;
import Models.Person;
import Models.User;

public class LoadRequest {
    /**
     * initialize lists of
     * events, users, people
     */

    ArrayList<Event> events;
    ArrayList<User> users;
    ArrayList<Person> persons;


    public LoadRequest(){

    }

    public LoadRequest(ArrayList<Event> e, ArrayList<Person> p, ArrayList<User> u){
        events = e;
        users = u;
        persons = p;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }
}
