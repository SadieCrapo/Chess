package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LoginRequest;
import result.LoginResult;
import service.UnauthorizedException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {

    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, DataAccessException {
        LoginResult result;

        LoginRequest request = getBody(req, LoginRequest.class);
        try {
            result = UserService.login(request);

        } catch (UnauthorizedException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
