package handler;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;
import service.BadRequestException;
import service.UserService;
import service.UsernameTakenException;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UsernameTakenException, BadRequestException {
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
