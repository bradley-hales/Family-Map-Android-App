package Models;

public class Event {
    /**
     * Event object with eventID (String), descendant (referencing the user),
     * person (reference to personID), latitude, longitude (double values),
     * country (String), city (String), eventType (name of event)
     * , year (int)
     */

    String eventID;
    String descendant;
    String personID;
    double latitude;
    double longitude;
    String country;
    String city;
    String eventType;
    Integer year;


    public Event(String evID, String desc, String psn, String lat, String lng,
                 String cnty, String cty, String evT, String yr){

        eventID = evID;
        descendant = desc;
        personID = psn;
        latitude = Double.parseDouble(lat);
        longitude = Double.parseDouble(lng);
        country = cnty;
        city = cty;
        eventType = evT;
        year = Integer.parseInt(yr);

    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPerson() {
        return personID;
    }

    public void setPerson(String person) {
        this.personID = person;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
