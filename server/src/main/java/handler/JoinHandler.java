package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.JoinRequest;
import result.JoinResult;
import server.Server;
import service.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class JoinHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) throws
            UnauthorizedException, UsernameTakenException, BadRequestException, DataAccessException {
        JoinResult result;
        String authToken;

        try {
            authToken = verifyAuth(req);
        } catch (UnauthorizedException e) {
            throw e;
        }

        JoinRequest request = getBody(req, JoinRequest.class);

        String username = Server.authDAO.getAuth(authToken).username();
        if (username == null) {
            throw new UnauthorizedException("No username in db");
        }
        try {
            result = GameService.joinGame(request, username);

        } catch (BadRequestException | UsernameTakenException | DataAccessException e) {
            throw e;
        }

        res.type("application/json");
        return new Gson().toJson(result);
    }
}
