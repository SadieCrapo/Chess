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
        res.body(new Gson().toJson(Map.of("message", "Error: unauthorized")));
        return new Gson().toJson(Map.of("message", "Error: unauthorized"));
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
