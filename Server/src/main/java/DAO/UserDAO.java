package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import Models.Event;
import Models.User;

public class UserDAO {
    /**
     * Handles all interaction
     * with the user table
     * in the database
     */
    Connection conn;

    public UserDAO(){}

    public User getUser(String username) throws DataBaseException{
        /**
         * get the specified user from the user table
         * given the username
         */
        String sql = "SELECT Password, Email, First_Name, Last_Name, " +
                "Gender, Person_ID FROM Users WHERE Username = ?";

        User user = null;
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();

                if (rs.isBeforeFirst()){
                    String password = rs.getString("Password");
                    String email = rs.getString("Email");
                    String firstName = rs.getString("First_name");
                    String lastName = rs.getString("Last_Name");
                    String gender = rs.getString("Gender");
                    String personID = rs.getString("Person_ID");

                    user = new User(username, password, email, firstName, lastName, gender, personID);
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to get user " + username + " " + e.getMessage());
        }

        return user;

    }
    public void createUser(User u)throws DataBaseException{
        /**
         * create the user in the database
         */
        String sql = "INSERT INTO Users (Username, Password, Email, First_Name, Last_Name," +
                " Gender, Person_ID) VALUES (?,?,?,?,?,?,?);";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, u.getUserName());
                stmt.setString(2, u.getPassword());
                stmt.setString(3, u.getEmail());
                stmt.setString(4, u.getFirstName());
                stmt.setString(5, u.getLastName());
                stmt.setString(6, u.getGender());
                stmt.setString(7, u.getPersonID());

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
            throw new DataBaseException("Failed to create user " + u.getUserName() + " " + e.getMessage());
        }
    }
    public List<User> getUsers(List<String> users){
        /**
         * Takes in a list of multiple usernames
         * and returns a list of user objects.
         * To be used with Fill
         */
        return null;
    }
    public void clear() throws DataBaseException{
        /**
         * clears the user table
         */

        String sql = "DELETE FROM Users;";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);

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
            throw new DataBaseException("Failed to delete User table " + " " + e.getMessage());
        }
    }

    boolean userInDB(String username){
        /**
         * checks to see if given user
         * is in the database,
         * returns true if user in database
         */
        return true;
    }

    boolean deleteUserData(String username){
        /**
         * deletes all user data for the
         * specified username,
         * returns true if successful
         */
        return true;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
