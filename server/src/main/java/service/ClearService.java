package service;

import result.ClearResult;
import server.Server;

public class ClearService {
    public static ClearResult clear() {

        Server.userDAO.clear();
        Server.gameDAO.clear();
        Server.authDAO.clear();

        return new ClearResult();
    }
}
