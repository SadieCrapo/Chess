package service;

import model.GameData;
import request.CreateRequest;
import result.CreateResult;
import result.ListResult;
import server.Server;

import java.util.ArrayList;

public class GameService {
    private static int nextID = 1;
    public static ListResult listGames() {
        ArrayList<GameData> gameList = Server.gameDAO.listGames();
        return new ListResult(gameList);
    }

    public static CreateResult createGame(CreateRequest request) throws BadRequestException {
        GameData game = new GameData(nextID++, null, null, request.gameName(), null);
        if (Server.gameDAO.getGame(game.gameID()) != null) {
            throw new BadRequestException("Already a game with this id");
        }
        Server.gameDAO.createGame(game);
        return new CreateResult(game.gameID());
    }

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
