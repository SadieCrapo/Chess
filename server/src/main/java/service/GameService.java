package service;

import model.AuthData;
import model.GameData;
import model.UserData;
import request.CreateRequest;
import request.JoinRequest;
//import request.ListRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;
import result.RegisterResult;
import server.Server;

import java.util.ArrayList;

public class GameService {
//    public static ListResult listGames(ListRequest) {
public static ListResult listGames() {
    ArrayList<GameData> gameList = Server.gameDAO.listGames();
        return new ListResult(gameList);
    }

//    public static CreateResult createGame(CreateRequest) {
//
//    }
//
//    public static JoinResult joinGame(JoinRequest) {
//
//    }

//    public static RegisterResult register(RegisterRequest request) throws UsernameTakenException, BadRequestException {
//        String username = request.username();
//        if (username == null || request.password() == null || request.email() == null) {
//            throw new BadRequestException("All fields must be completed");
//        }
//        UserData user = new UserData(username, request.password(), request.email());
//        if (Server.userDAO.getUser(username) != null) {
//            throw new UsernameTakenException("Username has already been claimed");
//        }
//
//        Server.userDAO.createUser(user);
//
//        String token = createAuthToken();
//        Server.authDAO.createAuth(new AuthData(token, username));
//
//        return new RegisterResult(username, token);
//    }
}
