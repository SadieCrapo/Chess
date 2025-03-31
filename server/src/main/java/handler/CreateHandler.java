package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.CreateRequest;
import result.CreateResult;
import service.GameService;
import exception.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, DataAccessException {
        CreateResult result;
        String authToken;

        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

        CreateRequest request = getBody(req, CreateRequest.class);

        try {
            result = GameService.createGame(request);

        } catch (DataAccessException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
