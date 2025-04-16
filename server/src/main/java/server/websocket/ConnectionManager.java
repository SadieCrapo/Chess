package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import websocket.messages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger("ConnectionManager");

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeUsername, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(message.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void send(String username, ServerMessage message) throws IOException {
        Connection connection = connections.get(username);
        if (connection != null && connection.session.isOpen()) {
            connection.send(new Gson().toJson(message));
        } else {
            connections.remove(username);
        }
    }
}