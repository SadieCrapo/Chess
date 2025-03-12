package handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import server.Server;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UsernameTakenException;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class Handler {
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, UsernameTakenException, BadRequestException, DataAccessException {
        return null;
    }

    public String verifyAuth(Request req) throws UnauthorizedException, DataAccessException {
        String authToken = "";
        try {
            authToken = new Gson().fromJson(req.headers("authorization"), String.class);
        } catch (JsonSyntaxException e) {
            throw new UnauthorizedException("Invalid authToken cannot contain whitespace");
        }
        if (authToken == null) {
                throw new UnauthorizedException("No authToken provided");
            }
        if (Server.authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedException("AuthToken not in db");
        }
            return authToken;
        }

    protected static <T> T getBody(Request req, Class<T> classType) {
        var body = new Gson().fromJson(req.body(), classType);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}

