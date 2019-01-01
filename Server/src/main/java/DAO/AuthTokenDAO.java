package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Models.AuthToken;

public class AuthTokenDAO {
    /**
     * Handles all interaction
     * with the database that deal
     * with auth tokens
     */
    Connection conn;


    public AuthTokenDAO(){
    }

    public AuthToken getAuthToken(String authTok) throws DataBaseException{
        /**
         * gets the specified auth token
         * for the given username from DB
         */
        String sql = "SELECT AuthToken, Username, PersonID FROM AuthToken WHERE AuthToken = ?";
        AuthToken authToken = null;
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, authTok);
                ResultSet res = stmt.executeQuery();

                if (res.isBeforeFirst()){
                    String auth = res.getString("AuthToken");
                    String user = res.getString("Username");
                    String pID = res.getString("PersonID");

                    authToken = new AuthToken();
                    authToken.setUUID(auth);
                    authToken.setUserName(user);
                    authToken.setPersonID(pID);
                }

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Could not get authtoken " + e.getMessage());
        }

        return authToken;
    }

    public void createAuthToken(AuthToken auth) throws DataBaseException{
        /**
         * puts the given auth token
         * in the database
         * returns true if successful
         */
//        Statement stmt = null;
//        try {
//            stmt = conn.createStatement();
//            stmt.executeUpdate(sql);
        String sql = "INSERT INTO AuthToken (AuthToken, Username, PersonID) VALUES (?,?,?);";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, auth.getUUID());
                stmt.setString(2, auth.getUserName());
                stmt.setString(3, auth.getPersonID());

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
            throw new DataBaseException("Data base didn't update correctly or connection not valid "
                    + e.getMessage());
        }

    }

    public void clear() throws DataBaseException{
        /**
         * clears the authtoken table
         * and returns true if successful
         */

        String sql = "DELETE FROM AuthToken;";
        try{
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }

        } catch (SQLException e) {
            throw new DataBaseException("Failed to clear AuthToken table " + e.getMessage());
        }
    }


    public void setConn(Connection conn) {
        this.conn = conn;
    }




//    "CREATE TABLE AuthToken (AuthToken TEXT NOT NULL," +
//            " Username TEXT NOT NULL, TimeStamp TEXT NOT NULL, " +
//            " PersonID  TEXT NOT NULL, " +
//            "FOREIGN KEY(Username) REFERENCES Users(Username)," +
//            "FOREIGN KEY(PersonID) REFERENCES Users(Person_ID)," +
//            " PRIMARY KEY(AuthToken)";
}
