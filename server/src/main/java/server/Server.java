package server;

import dataaccess.*;
import handler.*;
import exception.BadRequestException;
import exception.UnauthorizedException;
import exception.UsernameTakenException;
import spark.*;

public class Server {
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static LoginHandler loginHandler;
    public static RegisterHandler registerHandler;
    public static LogoutHandler logoutHandler;
    public static ClearHandler clearHandler;
    public static ListHandler listHandler;
    public static CreateHandler createHandler;
    public static JoinHandler joinHandler;

    public static ErrorHandler errorHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        try {
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        } catch (Throwable ex) {
            System.out.printf("Error: %s%n", ex.getMessage());
        }

        loginHandler = new LoginHandler();
        registerHandler = new RegisterHandler();
        logoutHandler = new LogoutHandler();
        clearHandler = new ClearHandler();
        listHandler = new ListHandler();
        createHandler = new CreateHandler();
        joinHandler = new JoinHandler();

        errorHandler = new ErrorHandler();

        Spark.post("/session", (req, res) -> (loginHandler.handleRequest(req, res)));
        Spark.post("/user", (req, res) -> (registerHandler.handleRequest(req, res)));
        Spark.delete("/session", (req, res) -> (logoutHandler.handleRequest(req, res)));
        Spark.delete("/db", (req, res) -> (clearHandler.handleRequest(req, res)));
        Spark.get("/game", (req, res) -> (listHandler.handleRequest(req, res)));
        Spark.post("/game", (req, res) -> (createHandler.handleRequest(req, res)));
        Spark.put("/game", (req, res) -> (joinHandler.handleRequest(req, res)));

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
