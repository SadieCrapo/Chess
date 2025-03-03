package server;

import dataaccess.*;
import handler.*;
import spark.*;

public class Server {
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static LoginHandler loginHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        Spark.post("/session", (req, res) -> (loginHandler.handleRequest(req, res)));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
