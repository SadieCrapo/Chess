package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
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
    public static ListResult listGames() throws DataAccessException {
        ArrayList<GameData> gameList = Server.gameDAO.listGames();
        return new ListResult(gameList);
    }

    public static CreateResult createGame(CreateRequest request) throws DataAccessException {
        GameData game = new GameData(1, null, null, request.gameName(), new ChessGame());

        return new CreateResult(Server.gameDAO.createGame(game));
    }

    public static JoinResult joinGame(JoinRequest request, String username) throws UsernameTakenException, BadRequestException, DataAccessException {
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
            System.out.println("Invalid user color " + request.playerColor());
            throw new BadRequestException("Invalid user color");
        }

        if (Objects.equals(request.playerColor(), "WHITE")) {
            newGame = new GameData(request.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            newGame = new GameData(request.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        }

        Server.gameDAO.updateGame(newGame);

        return new JoinResult(newGame);
    }
}
