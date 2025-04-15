package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import websocket.messages.LoadGameMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
//        if (message.contains("CONNECT")) {
//            connect("authToken", session);
//        }
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), session);
            case MAKE_MOVE -> makeMove();
            case RESIGN -> resign();
            case LEAVE -> leave();
        }
    }

    private void connect(String authToken, Session session) throws IOException {
        connections.add(authToken, session);
        var message = String.format("%s has joined the game", authToken);
        var notification = new NotificationMessage(message);
        connections.broadcast(authToken, notification);
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
