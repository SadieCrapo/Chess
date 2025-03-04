package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UsernameTakenException;
import spark.*;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static LoginHandler loginHandler;
    public static RegisterHandler registerHandler;
    public static LogoutHandler logoutHandler;
    public static ClearHandler clearHandler;
    public static ListHandler listHandler;

    public static ErrorHandler errorHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        loginHandler = new LoginHandler();
        registerHandler = new RegisterHandler();
        logoutHandler = new LogoutHandler();
        clearHandler = new ClearHandler();
        listHandler = new ListHandler();

        errorHandler = new ErrorHandler();

        Spark.post("/session", (req, res) -> (loginHandler.handleRequest(req, res)));
        Spark.post("/user", (req, res) -> (registerHandler.handleRequest(req, res)));
        Spark.delete("/session", (req, res) -> (logoutHandler.handleRequest(req, res)));
        Spark.delete("/db", (req, res) -> (clearHandler.handleRequest(req, res)));
        Spark.get("/game", (req, res) -> (listHandler.handleRequest(req, res)));

        Spark.exception(UnauthorizedException.class, errorHandler::unauthorizedHandler);
        Spark.exception(UsernameTakenException.class, errorHandler::usernameTakenHandler);
        Spark.exception(BadRequestException.class, errorHandler::badRequestHandler);
        Spark.notFound(errorHandler::notFoundHandler);
        Spark.exception(Exception.class, errorHandler::errorHandler);

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
