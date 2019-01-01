package Services;

import DAO.AuthTokenDAO;
import DAO.DataBase;
import DAO.DataBaseException;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import Request.ClearRequest;
import Response.ClearResponse;

public class ClearService {
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and
     * generated person and event data
     */

    public ClearService(){

    }

    public ClearResponse clearAll(){
        /**
         * clear each table in the database
         * by calling clear function in Database package
         */
        DataBase db = new DataBase();
        ClearResponse response = new ClearResponse();
        try{
            db.openConnection();
            db.clearAll();
            db.closeConnection(true);
            response.setMessage("Clear successful");


        }catch (DataBaseException d){
            db.closeConnection(false);
            response.setMessage("clear failed");
            d.printStackTrace();
        }

        return response;
    }

}
