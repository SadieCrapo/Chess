package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import service.UserService;
import spark.Request;
import spark.Response;

import exception.UsernameTakenException;
import exception.BadRequestException;

public class RegisterHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UsernameTakenException, BadRequestException, DataAccessException {
        RegisterResult result;

        RegisterRequest request = getBody(req, RegisterRequest.class);
        try {
            result = UserService.register(request);
        } catch (UsernameTakenException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
