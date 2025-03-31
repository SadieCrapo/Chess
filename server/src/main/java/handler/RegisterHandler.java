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

        RegisterRequest request = getBody(req, RegisterRequest.class);
        RegisterResult result = UserService.register(request);

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
