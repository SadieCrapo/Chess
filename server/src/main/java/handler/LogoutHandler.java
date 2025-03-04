package handler;

import com.google.gson.Gson;
import request.LoginRequest;
import request.LogoutRequest;
import result.LoginResult;
import result.LogoutResult;
import service.UnauthorizedException;
import service.UserService;
import service.UsernameTakenException;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException {
        LogoutResult result;
        String authToken;

//        String authToken = req.headers("authorization");
//        verifyAuth(authToken);
        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

//        LogoutRequest request = getBody(req, LogoutRequest.class);
        LogoutRequest request = new LogoutRequest(authToken);
        try {
//            result = UserService.logout(request);
            result = UserService.logout(request);

        } catch (UnauthorizedException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
//        return new Gson().toJson("");
    }
}
