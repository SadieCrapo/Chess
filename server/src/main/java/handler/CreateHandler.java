package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import request.LoginRequest;
import request.LogoutRequest;
import result.CreateResult;
import result.LogoutResult;
import service.BadRequestException;
import service.GameService;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, BadRequestException {
        CreateResult result;
        String authToken;

//        String authToken = req.headers("authorization");
//        verifyAuth(authToken);
        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

//        LogoutRequest request = getBody(req, LogoutRequest.class);
        CreateRequest request = getBody(req, CreateRequest.class);

//        CreateRequest request = new CreateRequest(authToken);
        try {
//            result = UserService.logout(request);
            result = GameService.createGame(request);

        } catch (BadRequestException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
//        return new Gson().toJson("");
    }
}
