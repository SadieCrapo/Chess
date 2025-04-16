package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import server.Server;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private static final Logger logger = Logger.getLogger("WebSocketHandler");


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, UnauthorizedException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove(), session);
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> leave(command.getAuthToken(), command.getGameID(), session);
        }
    }

    private void connect(String authToken, int gameID, Session session) throws DataAccessException, UnauthorizedException, IOException {
        String username = authToken;
        try {
            username = Server.authDAO.getAuth(authToken).username();
            GameData gameData = Server.gameDAO.getGame(gameID);

            connections.add(username, session);
            var message = String.format("%s has joined the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
            var loadMessage = new LoadGameMessage(gameData.game());
            connections.send(username, loadMessage);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage(e.getMessage());
            connections.add(username, session);
            connections.send(username, errorMessage);
            connections.remove(username);
        }
    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) throws DataAccessException, UnauthorizedException, IOException {
        String username = authToken;
        try {
            username = Server.authDAO.getAuth(authToken).username();

            GameData gameData = Server.gameDAO.getGame(gameID);
            ChessGame game = gameData.game();

            ChessGame.TeamColor currentTeam = game.getTeamTurn();
            if (currentTeam == ChessGame.TeamColor.BLACK) {
                if (!username.equals(gameData.blackUsername())) {
                    var errorMessage = new ErrorMessage("Wrong team");
                    connections.send(username, errorMessage);
                    return;
                }
            } else if (!username.equals(gameData.whiteUsername())) {
                var errorMessage = new ErrorMessage("Wrong team");
                connections.send(username, errorMessage);
                return;
            }

            game.makeMove(move);
            GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            Server.gameDAO.updateGame(updatedGameData);

            var message = String.format("%s made move %s", username, move);
            var notification = new NotificationMessage(message);
            var loadMessage = new LoadGameMessage(game);
            connections.broadcast(username, notification);
            connections.broadcast("", loadMessage);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage(e.getMessage());
            connections.add(username, session);
            connections.send(username, errorMessage);
            connections.remove(username);
        } catch (InvalidMoveException e) {
            var errorMessage = new ErrorMessage(e.getMessage());
            connections.send(username, errorMessage);
        }
    }

    private void resign(String authToken, int gameID, Session session) throws DataAccessException, UnauthorizedException, IOException {
        String username = authToken;
        try {
            username = Server.authDAO.getAuth(authToken).username();
            GameData gameData = Server.gameDAO.getGame(gameID);
            ChessGame game = gameData.game();

            if (!(username.equals(gameData.blackUsername()) || username.equals(gameData.whiteUsername()))) {
                var errorMessage = new ErrorMessage("observer cannot resign");
                connections.send(username, errorMessage);
                return;
            }

            if (Objects.equals(game.getBoard(), new ChessBoard())) {
                var errorMessage = new ErrorMessage("cannot resign after opponent");
                connections.send(username, errorMessage);
                return;
            }

            game.setBoard(new ChessBoard());
            GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            Server.gameDAO.updateGame(updatedGameData);

//            connections.add(username, session);
            var message = String.format("%s has resigned the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast("", notification);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage(e.getMessage());
            connections.add(username, session);
            connections.send(username, errorMessage);
            connections.remove(username);
        }
    }

    private void leave(String authToken, int gameID, Session session) throws DataAccessException, UnauthorizedException, IOException {
        String username = authToken;
        try {
            username = Server.authDAO.getAuth(authToken).username();
            GameData gameData = Server.gameDAO.getGame(gameID);
            GameData updatedGameData;

            if (username.equals(gameData.blackUsername())) {
                updatedGameData = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            } else if (username.equals(gameData.whiteUsername())) {
                updatedGameData = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
            }
            Server.gameDAO.updateGame(updatedGameData);

            connections.remove(username);
            var message = String.format("%s has left the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage(e.getMessage());
            connections.add(username, session);
            connections.send(username, errorMessage);
            connections.remove(username);
        }
    }
}
