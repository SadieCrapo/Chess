package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
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

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

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

            connections.add(username, session, gameID);
            var message = String.format("%s has joined the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification, gameID);
            var loadMessage = new LoadGameMessage(gameData.game());
            connections.send(username, loadMessage);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage("Error: " + e.getMessage());
            connections.add(username, session, gameID);
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

            if (game.getBoard() == new ChessBoard()) {
                var errorMessage = new ErrorMessage("Error: can't move after player resigns");
                connections.send(username, errorMessage);
                return;
            }

            ChessGame.TeamColor currentTeam = game.getTeamTurn();
            if (currentTeam == ChessGame.TeamColor.BLACK) {
                if (!username.equals(gameData.blackUsername())) {
                    var errorMessage = new ErrorMessage("Error: not your turn");
                    connections.send(username, errorMessage);
                    return;
                }
            } else if (!username.equals(gameData.whiteUsername())) {
                var errorMessage = new ErrorMessage("Error: not your turn");
                connections.send(username, errorMessage);
                return;
            }

            game.makeMove(move);
            GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            Server.gameDAO.updateGame(updatedGameData);

            String parsedMove = parseMove(move);

            var message = String.format("%s made move %s", username, parsedMove);
            var notification = new NotificationMessage(message);
            var loadMessage = new LoadGameMessage(game);
            connections.broadcast("", loadMessage, gameID);
            connections.broadcast(username, notification, gameID);

            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                var checkmateMessage = String.format("Game over, %s is in checkmate", gameData.whiteUsername());
                connections.broadcast("", new NotificationMessage(checkmateMessage), gameID);
                return;
            } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                var checkMessage = String.format("%s is in check", gameData.whiteUsername());
                connections.broadcast("", new NotificationMessage(checkMessage), gameID);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                var checkmateMessage = String.format("Game over, %s is in checkmate", gameData.blackUsername());
                connections.broadcast("", new NotificationMessage(checkmateMessage), gameID);
                return;
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                var checkMessage = String.format("%s is in check", gameData.whiteUsername());
                connections.broadcast("", new NotificationMessage(checkMessage), gameID);
            }
            if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                var stalemateMessage = String.format("Game over, %s is in stalemate", gameData.blackUsername());
                connections.broadcast("", new NotificationMessage(stalemateMessage), gameID);
            } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
                var stalemateMessage = String.format("Game over, %s is in stalemate", gameData.blackUsername());
                connections.broadcast("", new NotificationMessage(stalemateMessage), gameID);
            }
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage("Error: " + e.getMessage());
            connections.add(username, session, gameID);
            connections.send(username, errorMessage);
            connections.remove(username);
        } catch (InvalidMoveException | BadRequestException e) {
            var errorMessage = new ErrorMessage("Error: " + e.getMessage());
            connections.send(username, errorMessage);
        }
    }

    private String parseMove(ChessMove move) throws BadRequestException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        int startRow = start.getRow();
        int startCol = start.getColumn();
        int endRow = end.getRow();
        int endCol = end.getColumn();

        String stringStartCol;
        String stringEndCol;

        stringStartCol = switch (startCol) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> throw new BadRequestException("col must be integer from 1-8");
        };
        stringEndCol = switch (endCol) {
            case 1 -> "a";
            case 2 -> "b";
            case 3 -> "c";
            case 4 -> "d";
            case 5 -> "e";
            case 6 -> "f";
            case 7 -> "g";
            case 8 -> "h";
            default -> throw new BadRequestException("col must be integer from 1-8");
        };
        String result = stringStartCol + String.valueOf(startRow) + " to " + stringEndCol + String.valueOf(endRow);

        return result;
    }

    private void resign(String authToken, int gameID, Session session) throws DataAccessException, UnauthorizedException, IOException {
        String username = authToken;
        try {
            username = Server.authDAO.getAuth(authToken).username();
            GameData gameData = Server.gameDAO.getGame(gameID);
            ChessGame game = gameData.game();

            if (!(username.equals(gameData.blackUsername()) || username.equals(gameData.whiteUsername()))) {
                var errorMessage = new ErrorMessage("Error: observer cannot resign");
                connections.send(username, errorMessage);
                return;
            }

            if (Objects.equals(game.getBoard(), new ChessBoard())) {
                var errorMessage = new ErrorMessage("Error: cannot resign after opponent");
                connections.send(username, errorMessage);
                return;
            }

            game.setBoard(new ChessBoard());
            GameData updatedGameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            Server.gameDAO.updateGame(updatedGameData);

//            connections.add(username, session);
            var message = String.format("%s has resigned the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast("", notification, gameID);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage("Error: " + e.getMessage());
            connections.add(username, session, gameID);
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
            connections.broadcast(username, notification, gameID);
        } catch (NullPointerException e) {
            var errorMessage = new ErrorMessage("Error: " + e.getMessage());
            connections.add(username, session, gameID);
            connections.send(username, errorMessage);
            connections.remove(username);
        }
    }
}
