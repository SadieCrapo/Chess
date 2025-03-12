package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import result.ListResult;
import service.*;
import spark.Request;
import spark.Response;

public class ListHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException, DataAccessException {
        ListResult result;
        String authToken;

        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

        result = GameService.listGames();

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
