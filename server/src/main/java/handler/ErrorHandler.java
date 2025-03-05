package handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.Map;

public class ErrorHandler {
    public Object unauthorizedHandler(Exception e, Request req, Response res) {
        return errorHelper(res, 401, "Error: unauthorized");
    }

    public Object usernameTakenHandler(Exception e, Request req, Response res) {
        return errorHelper(res, 403, "Error: already taken");
    }

    public Object badRequestHandler(Exception e, Request req, Response res) {
        return errorHelper(res, 400, "Error: bad request");
    }

    public Object notFoundHandler(Request req, Response res) {
        return errorHelper(res, 400, String.format("[%s] %s not found", req.requestMethod(), req.pathInfo()));
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        return errorHelper(res, 500, String.format("Error: %s", e.getMessage()));
    }

    public Object errorHelper(Response res, int status, String msg) {
        res.type("application/json");
        res.status(status);
        var body = new Gson().toJson(Map.of("message", msg, "success", false));
        res.body(body);
        return body;
    }
}
