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

import Models.Event;
import Models.Person;

public class PersonDAO {
    /**
     * Handles all interaction
     * with the Person table
     * in the database
     */
    Connection conn;


    public PersonDAO(){}

    public void createPerson(Person p) throws DataBaseException{
        /**
         * creates the person in the database
         */
        String sql = "INSERT INTO Persons (Person_ID, Descendant, First_Name, Last_Name, Gender," +
                " Father, Mother, Spouse) VALUES (?,?,?,?,?,?,?,?);";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, p.getPersonID());
                stmt.setString(2, p.getDescendant());
                stmt.setString(3, p.getFirstName());
                stmt.setString(4, p.getLastName());
                stmt.setString(5, p.getGender());
                stmt.setString(6, p.getFather());
                stmt.setString(7, p.getMother());
                stmt.setString(8, p.getSpouse());

                // update in table
                stmt.executeUpdate();

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to create Person " + p.getFirstName() + " " + e.getMessage());
        }
    }

    public Person getPerson(String personID) throws DataBaseException{
        /**
         * get the specified person from the database
         */

        String sql = "SELECT Descendant, First_Name, Last_Name, Gender, " +
                     "Father, Mother, Spouse FROM Persons WHERE Person_ID = ?";

        Person person = null;
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, personID);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()){
                    String descendant = rs.getString("Descendant");
                    String firstName = rs.getString("First_Name");
                    String lastName = rs.getString("Last_Name");
                    String gender = rs.getString("Gender");
                    String father = rs.getString("Father");
                    String mother = rs.getString("Mother");
                    String spouse = rs.getString("Spouse");

                    person = new Person(personID, descendant, firstName, lastName,
                            gender, father, mother, spouse);
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to get person " + personID + " " + e.getMessage());
        }

        return person;
    }

    public ArrayList<Person> getAllPeople(String username) throws DataBaseException{
        /**
         * get the entire family tree of the specified user
         * and return them as set of people
         */

        String sql = "SELECT Person_ID FROM Persons WHERE Descendant = ?";

        ArrayList<Person> personList = null;

        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()){
                    // loop through result set and add events to list
                    personList = new ArrayList<>();
                    while(rs.next()){
                        String personID = rs.getString("Person_ID");
                        Person p = getPerson(personID);
                        personList.add(p);
                    }
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }

        return personList;
    }

    public void deleteByUser(String username) throws DataBaseException {

        String sql = "DELETE FROM Persons WHERE Descendant = ?";
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

    public void clear() throws DataBaseException{
        /**
         * clear the person table
         */

        String sql = "DELETE FROM Persons;";
        try{
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to clear Person table " + e.getMessage());
        }
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }






//    "CREATE TABLE Persons (Person_IDvTEXT NOT NULL, " +
//            "Descendant TEXT NOT NULL, First_Name TEXT NOT NULL, " +
//            "Last_Name TEXT NOT NULL, Gender TEXT NOT NULL, Father TEXT, " +
//            "Mother TEXT, Spouse TEXT, FOREIGN KEY(Descendant) REFERENCES Users(Username), " +
//            "FOREIGN KEY(Mother) REFERENCES Persons(Person_ID), " +
//            "FOREIGN KEY(Father) REFERENCES Persons(Person_ID), " +
//            "FOREIGN KEY(Spouse) REFERENCES Persons(Person_ID), " +
//            "PRIMARY KEY(Person_ID)";
}
