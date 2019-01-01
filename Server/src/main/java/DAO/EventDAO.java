package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.crypto.Data;

import Models.AuthToken;
import Models.Event;

public class EventDAO {
    /**
     * handles all interaction
     * with the Event table in
     * the database
     */
    Connection conn;

    public EventDAO() {
    }

    public void createEvent(Event ev) throws DataBaseException {
        /**
         * create the specified event
         * in the database
         */
        String sql = "INSERT INTO Events (Event_ID, Descendant, Person, Latitude, Longitude," +
                " Country, City, Event_Type, Year) VALUES (?,?,?,?,?,?,?,?,?);";
        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, ev.getEventID());
                stmt.setString(2, ev.getDescendant());
                stmt.setString(3, ev.getPerson());
                stmt.setDouble(4, ev.getLatitude());
                stmt.setDouble(5, ev.getLongitude());
                stmt.setString(6, ev.getCountry());
                stmt.setString(7, ev.getCity());
                stmt.setString(8, ev.getEventType());
                stmt.setInt(9, ev.getYear());

                // update in table
                stmt.executeUpdate();

            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to create event " + ev.getEventType() + " " + e.getMessage());
        }
    }

    public Event getEvent(String eventID) throws DataBaseException {
        /**
         * get the specified event from the database
         */
        String sql = "SELECT Event_ID, Descendant, Person, Latitude, Longitude, " +
                "Country, City, Event_Type, Year FROM Events WHERE Event_ID = ?";

        Event ev = null;
        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, eventID);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()) {
                    String event = rs.getString("Event_ID");
                    String descendant = rs.getString("Descendant");
                    String person = rs.getString("Person");
                    String lat = String.valueOf(rs.getDouble("Latitude"));
                    String lng = String.valueOf(rs.getDouble("Longitude"));
                    String country = rs.getString("Country");
                    String city = rs.getString("City");
                    String evType = rs.getString("Event_Type");
                    String year = Integer.toString(rs.getInt("Year"));

                    ev = new Event(event, descendant, person, lat, lng, country, city, evType, year);
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to get event " + eventID + " " + e.getMessage());
        }

        return ev;
    }

    public ArrayList<Event> getEvents(String user) throws DataBaseException {
        /**
         * gets all events of all family members
         * for the given user
         * and returns them as a list
         */
        String sql = "SELECT Event_ID FROM Events WHERE Descendant = ?";

        ArrayList<Event> eventList = null;

        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, user);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()) {
                    eventList = new ArrayList<>();
                    // loop through result set and add events to list
                    while (rs.next()) {
                        String eventID = rs.getString("Event_ID");
                        Event ev = getEvent(eventID);
                        eventList.add(ev);
                    }
                }

            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return eventList;

    }

    // delete any data associated with
    // the given username
    public void deleteByUser(String username) throws DataBaseException{
        String sql = "DELETE FROM Events WHERE Descendant = ?";
        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);

                // update in table
                stmt.executeUpdate();


            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to clear Events table for user " + username + " " + e.getMessage());
        }

    }

    public void clear() throws DataBaseException {
        /**
         * clear the event table
         */
        String sql = "DELETE FROM Events;";
        try {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();

            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to clear Events table " + e.getMessage());
        }
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}





//    "CREATE TABLE Events (Event_ID	TEXT NOT NULL UNIQUE," +
//            " Descendant TEXT NOT NULL, Person TEXT NOT NULL, Latitude	REAL NOT NULL," +
//            " Longitude REAL NOT NULL, Country	TEXT NOT NULL, City TEXT NOT NULL, " +
//            "Event_Type TEXT NOT NULL, Year INTEGER NOT NULL, " +
//            "FOREIGN KEY(Descendant) REFERENCES Users(Username), " +
//            "PRIMARY KEY(Event_ID), FOREIGN KEY(Person) REFERENCES Persons(Person_ID))";
//}
