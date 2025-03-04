package handler;

import com.google.gson.Gson;
import result.ClearResult;
import service.ClearService;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws UnauthorizedException {
        ClearResult result = ClearService.clear();

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
