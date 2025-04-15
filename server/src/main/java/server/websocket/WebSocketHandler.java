package server.websocket;

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

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, UnauthorizedException, IOException {
//        if (message.contains("CONNECT")) {
//            connect("authToken", session);
//        }
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove();
            case RESIGN -> resign();
            case LEAVE -> leave();
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

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }

    private void makeMove() {

    }

    private void resign() {

    }

    private void leave() {

    }
}
