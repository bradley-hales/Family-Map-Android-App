package com.example.bradl.familymapserver;

import android.graphics.Color;
import android.graphics.drawable.shapes.PathShape;
import android.provider.Contacts;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Models.Event;
import Models.Person;

public class Model {

    private static Model instance = null;
    private static Settings settings = null;
    static HashMap<String, Person> people;
    static HashMap<String, Event> events;
    static HashMap<String, Person> fatherSide;
    static HashMap<String, Person> motherSide;
    static ArrayList<Event> fatherSideEvent;
    static ArrayList<Event> motherSideEvent;
    static HashMap<String, Event> fathersSideEvents;
    static HashMap<String, Event> mothersSideEvents;

    static ArrayList<Person> male;
    static ArrayList<Person> female;
    static ArrayList<Person> allPeople;
    static ArrayList<Event> maleEvent;
    static ArrayList<Event> femaleEvent;
    static HashMap<String, Event> baptism;
    static HashMap<String, Event> birth;
    static HashMap<String, Event> marriage;
    static HashMap<String, Event> death;
    static String authToken;
    static String currUser;
    static String personID;
    static String port;
    static String host;
    static HashMap<String, Event> filteredEvents;
    static HashMap<String, Person> filteredPeople;
    static HashMap<String, Boolean> whatTypesInFilteredEvents;
    static ArrayList<Event> selectedPersonEvents;
    static HashMap<String, ArrayList<Event>> eachEventTypeArray;
    static HashMap<String, ArrayList<Event>> removedEvents;
    static Person selectedPerson;
    static String userBirth;
    static ArrayList<String> eventTypes;
    static ArrayList<String> searchResults = null;
    static String searchText = null;

    private Model(){
//        settings = new Settings();
        removedEvents = new HashMap<>();
        searchResults = new ArrayList<>();
    }

    public static Model getInstance(){
        if (instance == null){
            instance = new Model();
        }
        return instance;
    }

    public Settings getSettings(){
        if (settings == null){
            settings = new Settings();
        }
        return settings;
    }

    public class Settings {
        String mapType;
        List<String> colorChoices = new ArrayList<>(Arrays.asList("red", "blue", "green", "yellow", "orange", "cyan", "azure"));
        List<Float> colorFloats = new ArrayList<>(Arrays.asList(0.0f, 240.0f, 120.0f, 60.0f, 30.0f, 180.0f, 210.0f, 40.0f, 50.0f, 170.0f, 200.0f, 300.0f));
        HashMap<String, Integer> eventGroupToLineColor;
        HashMap<String, Integer> lineColors;
        HashMap<String, Float> defaultColors;
        HashMap<String, Float> colorValues;
        HashMap<String, Float> markerColors;
        HashMap<String, Boolean> drawLine;
        HashMap<String, Integer> colorPositions;
        HashMap<String, Integer> mapTypePositions;
        String defaultMapType = "normal";


        public Settings(){

            defaultColors = new HashMap<>();
            colorValues = new HashMap<>();
            lineColors = new HashMap<>();
            eventGroupToLineColor = new HashMap<>();
            drawLine = new HashMap<>();
            colorPositions = new HashMap<>();
            mapTypePositions = new HashMap<>();

            mapTypePositions.put("normal", 0);
            mapTypePositions.put("hybrid", 1);
            mapTypePositions.put("satellite", 2);
            mapTypePositions.put("terrain", 3);

            colorPositions.put("red", 0);
            colorPositions.put("green", 1);
            colorPositions.put("blue", 2);

            drawLine.put("life story", true);
            drawLine.put("spouse", true);
            drawLine.put("father side", true);
            drawLine.put("mother side", true);

            eventGroupToLineColor.put("life story", Color.RED);
            eventGroupToLineColor.put("spouse", Color.GREEN);
            eventGroupToLineColor.put("father side", Color.BLUE);
            eventGroupToLineColor.put("mother side", Color.BLUE);

            lineColors.put("red", Color.RED);
            lineColors.put("green", Color.GREEN);
            lineColors.put("blue", Color.BLUE);

            colorValues.put("red", 0.0f);
            colorValues.put("blue", 240.0f);
            colorValues.put("green", 120.0f);
            colorValues.put("yellow", 60.0f);
            colorValues.put("orange", 30.0f);
            colorValues.put("cyan", 180.0f);
            colorValues.put("azure", 210.0f);

            setMarkerDefaultColors();

        }

        public String getMapType() {
            return defaultMapType;
        }

        public void setMapType(String maptype) {
            this.mapType = maptype;
        }


        public List<String> getColorChoices() {
            return colorChoices;
        }

        public void setMarkerDefaultColors(){
            markerColors = new HashMap<>();

            for (int i = 0; i < eventTypes.size(); i++){
                 markerColors.put(eventTypes.get(i), colorFloats.get(i));
            }
        }

        public HashMap<String, Float> getDefaultColors() {
            return defaultColors;
        }

        public HashMap<String, Float> getLineColorValues() {
            return colorValues;
        }

        public HashMap<String, Float> getMarkerColors() {
            return markerColors;
        }

        public HashMap<String, Integer> getLineColors() {
            return lineColors;
        }

        public HashMap<String, Integer> getEventGroupToLineColor() {
            return eventGroupToLineColor;
        }

        public HashMap<String, Boolean> getDrawLine() {
            return drawLine;
        }

        public void changeLinesDrawn(String type, boolean isChecked){
            drawLine.put(type.toLowerCase(), isChecked);
        }
        public void changeEventGroupToLineColor(String type, String color){
            Integer newColor = lineColors.get(color.toLowerCase());
            eventGroupToLineColor.put(type.toLowerCase(), newColor);
        }

        public int getColorPositions(String type) {
            int colorVal = eventGroupToLineColor.get(type.toLowerCase());

            if (colorVal == Color.RED){
                return colorPositions.get("red");
            }
            else if (colorVal == Color.GREEN){
                return colorPositions.get("green");
            }
            else {
                return colorPositions.get("blue");
            }
        }
        public int getMapTypePosition(){
            return mapTypePositions.get(defaultMapType);
        }
        public void changeMapType(String type){
            defaultMapType = type.toLowerCase();
        }
        public boolean getSwitchState(String type){
            return drawLine.get(type.toLowerCase());
        }
    }

    public static void setAuthToken(String authToken) {
        Model.authToken = authToken;
    }

    public static void setCurrUser(String currUser) {
        Model.currUser = currUser;
    }

    public static void setPersonID(String personID) {
        Model.personID = personID;
    }

    public static void fillPeople(ArrayList<Person> pArray){
        // fills the people map with users family

        // initialize people map
        people = new HashMap<>();
        male = new ArrayList<>();
        female = new ArrayList<>();
        allPeople = new ArrayList<>();

        // fill people map
        for(Person p: pArray){
            people.put(p.getPersonID(), p);
            allPeople.add(p);

            if (p.getGender().equals("m")){
                male.add(p);
            }
            else if (p.getGender().equals("f")){
                female.add(p);
            }
        }

        // set filteredPeople to all People till filter is updated
        filteredPeople = people;
    }
    public static ArrayList<Person> getArrayAllPeople(){
        ArrayList<Person> pArray = new ArrayList<>();
        for (Map.Entry<String, Person> pair: filteredPeople.entrySet()){
            Person p = pair.getValue();
            pArray.add(p);
        }

        return pArray;
    }

    public void fillEvents(ArrayList<Event> eArray){ // was static
        // fills the events map with the users family events


        // initialize event map
        events = new HashMap<>();
        eventTypes = new ArrayList<>();
        birth = new HashMap<>();
        baptism = new HashMap<>();
        marriage = new HashMap<>();
        death = new HashMap<>();
        selectedPersonEvents = new ArrayList<>();
        eachEventTypeArray = new HashMap<>();
        // fill events maps
        for(Event e: eArray){
            events.put(e.getEventID(), e);
            // get unique event types
            if (!eventTypes.contains(e.getEventType().toLowerCase())){
                eventTypes.add(e.getEventType().toLowerCase());
            }


            // fill map with all event types and their corresponding events
            if (!eachEventTypeArray.containsKey(e.getEventType().toLowerCase())){
                ArrayList<Event> ev = new ArrayList<>();
                ev.add(e);
                eachEventTypeArray.put(e.getEventType().toLowerCase(), ev);
            }
            else{
                eachEventTypeArray.get(e.getEventType().toLowerCase()).add(e);
            }
        }

        settings = new Settings();

        // set filteredEvents to all events till filter is changed
        filteredEvents = events;

        // set users events and birth
        selectedPersonEvents = getPersonEvents(personID);
        userBirth = selectedPersonEvents.get(0).getEventID();


    }

    public static void fillDictionairies(){
        // fills the other dictionaries i.e.
        // fatherSide, motherSide ...
        // with the appropriate data

        // get user's father and mother ID
        Person user =  filteredPeople.get(personID);
        String father = user.getFather();
        String mother = user.getMother();

        fatherSide = new HashMap<>();
        fatherSideEvent = new ArrayList<>();
        motherSideEvent = new ArrayList<>();
        // TODO: not sure to use
        fathersSideEvents = new HashMap<>();
        mothersSideEvents = new HashMap<>();

        motherSide = new HashMap<>();
        maleEvent = new ArrayList<>();
        femaleEvent = new ArrayList<>();

        getFatherSide(father);
        getMotherSide(mother);

        fillMaleFemale();

        // add Father/mother side, male and female to eachEventTypeArray
        eachEventTypeArray.put("male", maleEvent);
        eachEventTypeArray.put("female", femaleEvent);
        eachEventTypeArray.put("fathers side", fatherSideEvent);
        eachEventTypeArray.put("mothers side", motherSideEvent);

        // set whatTypesInFilteredEvents
        if (whatTypesInFilteredEvents == null){
            whatTypesInFilteredEvents = new HashMap<>();
            // add Father/mother side, male, female
            whatTypesInFilteredEvents.put("fathers side", true);
            whatTypesInFilteredEvents.put("mothers side", true);
            whatTypesInFilteredEvents.put("male", true);
            whatTypesInFilteredEvents.put("female", true);
        }
        for (String type: eventTypes){
            if (!whatTypesInFilteredEvents.containsKey(type)){
                whatTypesInFilteredEvents.put(type, true);
            }
        }

    }

    private static void getFatherSide(String fID){
        // get father's side as long
        // as there is an ID

        if (fID != null){
            Person relative = filteredPeople.get(fID);
            fatherSide.put(fID, relative);
            // add all events for
            // this person to fatherSideEvents
            ArrayList<Event> evs = getPersonEvents(fID);
            for (Event e: evs){
                fatherSideEvent.add(e);
                fathersSideEvents.put(e.getEventID(), e);
            }

            // get father and mother of given person
            String m = relative.getMother();
            String f = relative.getFather();
            getFatherSide(m);
            getFatherSide(f);
        }

    }

    private static void getMotherSide(String mID){
        // get mother's side as long
        // as there is an ID

        if (mID != null){
            Person relative = filteredPeople.get(mID);
            motherSide.put(mID, relative);
            // add all events for
            // this person to motherSideEvents
            ArrayList<Event> evs = getPersonEvents(mID);
            for (Event e: evs){
                motherSideEvent.add(e);
                mothersSideEvents.put(e.getEventID(), e);
            }

            String m = relative.getMother();
            String f = relative.getFather();
            getMotherSide(m);
            getMotherSide(f);
        }
    }

    public static void applyFilterToDictionaries(String evType, boolean checked){
        // update all dictionaries with new filter

//         the updated switch
        whatTypesInFilteredEvents.put(evType, checked);

        // make eventType compatable
        String type;
        if (evType.equals("male") || evType.equals("female")){
            type = evType.substring(0,1);
        }
        else {
            type = evType;
        }


        HashMap<String, Event> addedEvents = new HashMap<>();

        if (checked){
            // add this event type
            ArrayList<Event> rEvents = removedEvents.get(evType);
            ArrayList<Event> eventsToRemove = new ArrayList<>();
            if (rEvents.size() != 0){
                for (Event e: rEvents){
                    boolean canAdd = true;
                    Person p = getPerson(e.getPerson());
                    for (Map.Entry<String, Boolean> pair: whatTypesInFilteredEvents.entrySet()){
                        boolean isChecked = pair.getValue();
                        String eType = pair.getKey();

                        if (eType.equals("male") || eType.equals("female")){
                            type = eType.substring(0,1);
                        }
                        else{
                            type = eType;
                        }

                        // we only care about the one that is not checked
                        if (isChecked == false){
                            if (type.equals("fathers side")){
                                if (fathersSideEvents.containsKey(e.getEventID())){
                                    canAdd = false;
                                }
                            }
                            else if (type.equals("mothers side")){
                                if (mothersSideEvents.containsKey(e.getEventID())){
                                    canAdd = false;
                                }
                            }
                            else if (e.getEventType().toLowerCase().equals(type)){
                                canAdd = false;
                            }
                            else if (p.getGender().equals(type)){
                                canAdd = false;
                            }
                        }
                    }

                    // check if we can add
                    if (canAdd){
                        filteredEvents.put(e.getEventID(), e);
//                        rEvents.remove(e);
                        eventsToRemove.add(e);
                        // TODO: might need to make sure event is removed from array
                    }
                }
                rEvents.removeAll(eventsToRemove);
            }

        }
        else{
            // take away specified event type
            if (!removedEvents.containsKey(evType)){
                removedEvents.put(evType, new ArrayList<Event>());
            }
            for (Map.Entry<String, Event> pair: filteredEvents.entrySet()){
                String eID = pair.getKey();
                Event ev = pair.getValue();
                Person p = getPerson(ev.getPerson());
                boolean canAdd = true;

                if (type.equals("fathers side")){
                    if (fathersSideEvents.containsKey(eID)){
                        canAdd = false;
                    }
                }
                else if (type.equals("mothers side")){
                    if (mothersSideEvents.containsKey(eID)){
                        canAdd = false;
                    }
                }
                else if (ev.getEventType().toLowerCase().equals(type)){
                    canAdd = false;
                }
                else if (p.getGender().equals(type)){
                    canAdd = false;
                }

                // check if you can add
                if (canAdd){
                    addedEvents.put(eID, ev);
                }
                else{
                    removedEvents.get(evType).add(ev);
                }
            }
            filteredEvents.clear();
            filteredEvents = addedEvents;
        }

    }

    private static void fillMaleFemale(){
        // fill the male and female event dictionaries

        for (Person m: male){
            ArrayList<Event> mEv = getPersonEvents(m.getPersonID());
            for (Event e: mEv){
                maleEvent.add(e);
            }
        }
        for (Person f: female){
            ArrayList<Event> fEv = getPersonEvents(f.getPersonID());
            for (Event e: fEv){
                femaleEvent.add(e);
            }
        }
    }

    public static Event getEventByID(String id){
        return filteredEvents.get(id);
    }

    public static ArrayList<Event> getPersonEvents(String pID){
        ArrayList<Event> eventList = new ArrayList<>();
        ArrayList<Event> sortedList;

        for (Map.Entry<String, Event> pair: filteredEvents.entrySet()){
            String key = pair.getKey();
            Event e = pair.getValue();

            if (e.getPerson().equals(pID)){
                eventList.add(e);
            }
        }

        sortedList = sortEventList(eventList);

        return sortedList;
    }
    public static ArrayList<Event> getTypeOfEvent(String evType){
        // get all of the specified type of events
        ArrayList<Event> eType = new ArrayList<>();
        for (Map.Entry<String, Event> pair: filteredEvents.entrySet()){
            String key = pair.getKey().toLowerCase();
            Event e = pair.getValue();

            if (key.equals(evType.toLowerCase())){
                eType.add(e);
            }
        }

        return eType;
    }

    public static ArrayList<Event> sortEventList(ArrayList<Event> ev){
        ArrayList<Event> sortedEv = ev;

        Event temp;
        for (int i = 1; i < ev.size(); i++) {
            for(int j = i ; j > 0 ; j--){
                String type2 = sortedEv.get(j).getEventType().toLowerCase();
                String type1 = sortedEv.get(j-1).getEventType().toLowerCase();
                int year2 = sortedEv.get(j).getYear();
                int year1 = sortedEv.get(j-1).getYear();
                if (type2.equals("birth")){
                    temp = sortedEv.get(0);
                    sortedEv.set(0, sortedEv.get(j));
                    sortedEv.set(j, temp);
                }
                else if (type2.equals("death")){
                    temp = sortedEv.get(sortedEv.size()-1);
                    sortedEv.set(sortedEv.size()-1, sortedEv.get(j));
                    sortedEv.set(j, temp);
                }
                else if (year2 < year1){
                    temp = sortedEv.get(j);
                    sortedEv.set(j, sortedEv.get(j-1));
                    sortedEv.set(j-1, temp);
                }
                else if (year1 == year2){
                    // sort alphabetically
                    int compare = type2.compareTo(type1);
                    if (compare < 0){
                        temp = sortedEv.get(j-1);
                        sortedEv.set(j-1, ev.get(j));
                        sortedEv.set(j, temp);
                    }
                }
            }
        }
        return sortedEv;

    }
    public static boolean checkIfFilterItemActive(String evType){
        boolean isActive = whatTypesInFilteredEvents.get(evType);

        return isActive;
    }


    public static void setSelectedPersonEvents(String pID){
        selectedPersonEvents.clear();

        selectedPersonEvents = getPersonEvents(pID);
    }
    public static ArrayList<Event> getSelectedPersonEvents(){
        return selectedPersonEvents;
    }

    public static String getGender(String pID){
        Person p = filteredPeople.get(pID);
        return p.getGender();
    }

    public static String getMarkerName(String pID){
        Person p = filteredPeople.get(pID);
        String name = p.getFirstName() + " " + p.getLastName();
        return name;
    }


    public static String getName(){
        Person p = filteredPeople.get(personID);

        String first = p.getFirstName();
        String last = p.getLastName();
        String name = first + " " + last;

        return name;
    }

    public static String getPersonName(Person p){
        String first = p.getFirstName();
        String last = p.getLastName();
        String name = first + " " + last;

        return name;
    }

    public static String getEventDetails(Event e){
        String type = e.getEventType();
        String city = e.getCity();
        String cntry = e.getCountry();
        String year = Integer.toString(e.getYear());
        String event = type + ": " + city + ", " + cntry + " (" + year + ")";

        return event;
    }
    public static String getSelectedPersonName(){
        String first = selectedPerson.getFirstName();
        String last = selectedPerson.getLastName();
        String name = first + " " + last;

        return name;
    }

    public static Person getChild(String pID){
        // cycle through all people and see who's
        // mom or dad is the pID
        Person child = null;
        ArrayList<Person> personArray = getArrayAllPeople();
        for (Person p: personArray){
            if (p.getFather() != null){
                if (p.getFather().equals(pID)){
                    child = p;
                }
            }
            if (p.getMother() != null){
                if (p.getMother().equals(pID)){
                    child = p;
                }
            }
        }

        return child;
    }


    public static String getUserBirth(){
        return userBirth;
    }

    public static HashMap<String, Person> getPeople() {
        return people;
    }

    public static HashMap<String, Event> getEvents() {
        return events;
    }

    public static Person getPerson(String pID){
        return filteredPeople.get(pID);
    }

    public static Event getEvent(String eID){
        return filteredEvents.get(eID);
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static HashMap<String, Event> getFilteredEvents() {
        return filteredEvents;
    }

    public static void setFilteredEvents(HashMap<String, Event> filteredEvents) {
        Model.filteredEvents = filteredEvents;
    }

    public static HashMap<String, Person> getFilteredPeople() {
        return filteredPeople;
    }

    public static void setFilterdPeople(HashMap<String, Person> filterdPeople) {
        Model.filteredPeople = filterdPeople;
    }

    public static Person getSelectedPerson() {
        return selectedPerson;
    }

    public static void setSelectedPerson(String pID) {
        selectedPerson = filteredPeople.get(pID);
    }

    public static ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        Model.port = port;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Model.host = host;
    }

    public static ArrayList<String> getSearchResults() {
        return searchResults;
    }

    public static void setSearchResults(ArrayList<String> searchResults) {
        Model.searchResults = searchResults;
    }

    public static String getSearchText() {
        return searchText;
    }

    public static void setSearchText(String searchText) {
        Model.searchText = searchText;
    }
}
