package handler;

import com.google.gson.Gson;
import server.Server;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UsernameTakenException;
import spark.Request;
import spark.Response;

public class Handler {
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, UsernameTakenException, BadRequestException {
        return null;
    }

public String verifyAuth(Request req) throws UnauthorizedException {
    String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
//    if (authToken == null || Server.authDAO.getAuth(authToken).username() == null) {
    if (authToken == null) {
            throw new UnauthorizedException("No authToken provided");
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

