package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ClearHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException {
        ClearResult result = ClearService.clear();

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
