package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;
import exception.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, DataAccessException {
        LogoutResult result;
        String authToken;

        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

        LogoutRequest request = new LogoutRequest(authToken);
        try {
            result = UserService.logout(request);

        } catch (UnauthorizedException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
