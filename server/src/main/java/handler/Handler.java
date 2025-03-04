package handler;

import com.google.gson.Gson;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class Handler {
    public Object handleRequest(Request req, Response res) throws UnauthorizedException {
        return null;
    }

    protected static <T> T getBody(Request req, Class<T> classType) {
        var body = new Gson().fromJson(req.body(), classType);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}

