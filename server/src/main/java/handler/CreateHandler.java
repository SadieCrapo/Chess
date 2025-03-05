package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import result.CreateResult;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class CreateHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, BadRequestException {
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

        } catch (BadRequestException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
