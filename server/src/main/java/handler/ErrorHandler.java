package handler;

import com.google.gson.Gson;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ErrorHandler {
    public Object unauthorizedHandler(Exception e, Request req, Response res) {
        res.type("application/json");
        res.status(401);
        var body = new Gson().toJson(Map.of("message", "Error: unauthorized"));
        res.body(body);
        return body;
    }

    public Object usernameTakenHandler(Exception e, Request req, Response res) {
        res.type("application/json");
        res.status(403);
        var body = new Gson().toJson(Map.of("message", "Error: already taken"));
        res.body(body);
        return body;
    }

    public Object badRequestHandler(Exception e, Request req, Response res) {
        res.type("application/json");
        res.status(400);
        var body = new Gson().toJson(Map.of("message", "Error: bad request"));
        res.body(body);
        return body;
    }

    public Object notFoundHandler(Request req, Response res) {
            String msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            var body = new Gson().toJson(Map.of("message", msg));
            res.type("application/json");
            res.status(400);
            res.body(body);
            return body;
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }

    protected static <T> T getBody(Request req, Class<T> classType) {
        var body = new Gson().fromJson(req.body(), classType);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
