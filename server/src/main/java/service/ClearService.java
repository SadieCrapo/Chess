package service;

import dataaccess.DataAccessException;
import result.ClearResult;
import server.Server;

public class ClearService {
    public static ClearResult clear() throws DataAccessException {

        Server.userDAO.clear();
        Server.gameDAO.clear();
        Server.authDAO.clear();

        return new ClearResult();
    }
}
