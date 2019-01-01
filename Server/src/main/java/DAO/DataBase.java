package DAO;

import org.w3c.dom.UserDataHandler;

import java.sql.Connection;
import java.sql.*;
import java.util.*;

import Models.AuthToken;

public class DataBase {
    /**
     * class that handles all functionality
     * of working with the database and thus
     * works with the DAO classes.
     * Member attributes will be one
     * of each DAO object
     */
    AuthTokenDAO auth;
    EventDAO event;
    PersonDAO person;
    UserDAO user;
    private Connection conn;
    boolean commit = false;


    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataBase(){
        /**
         * open connection
         */
//        try{
        createDAO();
//            openConnection();
//            passConnection();
//        }catch (DataBaseException db){
//            db.printStackTrace();
//
//        }

    }
    public void createTables() throws DataBaseException{
//        String dropAuth = "DROP TABLE IF EXISTS AuthToken";
//        String dropPsn = "DROP TABLE IF EXISTS Persons";
//        String dropEv = "DROP TABLE IF EXISTS Events";
//        String dropUr = "DROP TABLE IF EXISTS Users";

        String auth =  "CREATE TABLE IF NOT EXISTS 'AuthToken' ('AuthToken' TEXT NOT NULL," +
            " 'Username' TEXT NOT NULL," +
            " 'PersonID'  TEXT NOT NULL, " +
            " PRIMARY KEY('AuthToken'))";
        String person =    "CREATE TABLE IF NOT EXISTS 'Persons' ('Person_ID' TEXT NOT NULL," +
                "'Descendant' TEXT NOT NULL,'First_Name' TEXT NOT NULL, 'Last_Name' TEXT NOT NULL," +
                "'Gender' TEXT NOT NULL,'Father' TEXT, 'Mother' TEXT, 'Spouse' TEXT, " +
                "PRIMARY KEY('Person_ID'))";

        String event = "CREATE TABLE IF NOT EXISTS 'Events' ('Event_ID' TEXT NOT NULL UNIQUE," +
                "'Descendant' TEXT NOT NULL, 'Person' TEXT NOT NULL, 'Latitude' REAL NOT NULL," +
                "'Longitude' REAL NOT NULL, 'Country' TEXT NOT NULL, 'City' TEXT NOT NULL, " +
                "'Event_Type' TEXT NOT NULL, 'Year' INTEGER NOT NULL, " +
                "PRIMARY KEY('Event_ID'))";
        String user = "CREATE TABLE IF NOT EXISTS 'Users' ('Username' TEXT NOT NULL UNIQUE, 'Password' TEXT NOT NULL," +
                "'Email' TEXT NOT NULL, 'First_Name' TEXT NOT NULL, 'Last_Name' TEXT NOT NULL, " +
                "'Gender' TEXT NOT NULL, 'Person_ID' TEXT NOT NULL, " +
                "PRIMARY KEY('Person_ID'))";
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
//                stmt.executeUpdate(dropAuth);
//                stmt.executeUpdate(dropEv);
//                stmt.executeUpdate(dropPsn);
//                stmt.executeUpdate(dropUr);
                stmt.executeUpdate(auth);
                stmt.executeUpdate(person);
                stmt.executeUpdate(event);
                stmt.executeUpdate(user);

            }
            finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e) {
            throw new DataBaseException("createTables failed " + e.getMessage());
        }


    }


    public void createDAO(){
        /**
         * create instance of each DAO
         */
        auth = new AuthTokenDAO();
        event = new EventDAO();
        person = new PersonDAO();
        user = new UserDAO();
    }
    public void openConnection() throws DataBaseException{
        /**
         * open a connection
         */
        try {
            final String CONNECTION_URL = "jdbc:sqlite:DB.db";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            auth.setConn(conn);
            event.setConn(conn);
            person.setConn(conn);
            user.setConn(conn);

            // Start a transaction
            conn.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new DataBaseException("openConnection failed" + " " + e);
        }
    }
    public void closeConnection(boolean commit){
        /**
         * close a connection,
         * boolean commit, boolean rollback,
         * just close connection
         */

        try {
            if (commit) {
                conn.commit();
            }
            else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void commit(){
        try{
            conn.commit();
        }catch (SQLException s){
            s.printStackTrace();
        }
    }

//    public void passConnection(){
//        /**
//         * gets the connection and
//         * passes it to each DAO
//         * object
//         */
//        auth.setConn(conn);
//        event.setConn(conn);
//        person.setConn(conn);
//        user.setConn(conn);
//    }

    public void clearAll() throws DataBaseException{
        try{
            auth.clear();
            event.clear();
            person.clear();
            user.clear();
        }catch (DataBaseException db){
            throw new DataBaseException(db.getMessage());
        }

    }

    public AuthTokenDAO getAuth() {
        return auth;
    }

    public EventDAO getEvent() {
        return event;
    }

    public PersonDAO getPerson() {
        return person;
    }

    public UserDAO getUser() {
        return user;
    }

    public Connection getConn() {
        return conn;
    }
}
