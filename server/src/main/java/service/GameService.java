package service;

import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;
import server.Server;

import java.util.ArrayList;
import java.util.Objects;

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

    public static JoinResult joinGame(JoinRequest request, String username) throws UsernameTakenException, BadRequestException {
        GameData game = Server.gameDAO.getGame(request.gameID());
        GameData newGame;

        if (game == null) {
            throw new BadRequestException("Game is null");
        }
        if (Objects.equals(request.playerColor(), "WHITE")) {
            if (game.whiteUsername() != null) {
                throw new UsernameTakenException("Username already taken");
            }
        } else if (Objects.equals(request.playerColor(), "BLACK")) {
            if (game.blackUsername() != null) {
                throw new UsernameTakenException("Username already taken");
            }
        } else {
            throw new BadRequestException("Invalid user color");
        }

        if (Objects.equals(request.playerColor(), "WHITE")) {
            newGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            newGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }

        Server.gameDAO.updateGame(newGame);

        return new JoinResult();
    }
}
