package repl;

import chess.ChessGame;
import client.Client;
import client.PostLoginClient;
import client.PreLoginClient;
import client.GamePlayClient;
import client.ServerFacade;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.BoardPrinter.printBoard;
import static ui.EscapeSequences.*;

public class REPL implements NotificationHandler {
    private Client client;
    private PreLoginClient preLoginClient;
    private PostLoginClient postLoginClient;
    private GamePlayClient gamePlayClient;

    private final ServerFacade server;

    public REPL(String serverUrl) {
        server = new ServerFacade(serverUrl);
        preLoginClient = new PreLoginClient(server, this);
        postLoginClient = new PostLoginClient(server, serverUrl, this);
        gamePlayClient = new GamePlayClient(server, this);
        client = preLoginClient;
    }

    public void run() {
        System.out.println("\u2654 Welcome to chess. Sign in to start. \u265A");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String setClientToPostLogin(String authToken) {
        client = postLoginClient;
        postLoginClient.setAuthToken(authToken);

        return client.eval("list");
    }

    public void setClientToPreLogin() {
        client = preLoginClient;
    }

    public void setClientToGamePlay(boolean observer, ChessGame game, int gameID, String team, WebSocketFacade ws, String authToken) {
        client = gamePlayClient;
        gamePlayClient.setObserver(observer);
        gamePlayClient.setGame(game);
        gamePlayClient.setGameID(gameID);
        gamePlayClient.setTeamColor(team);
        gamePlayClient.setWs(ws);
        gamePlayClient.setAuth(authToken);
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGameMessage message = (LoadGameMessage) notification;
            gamePlayClient.setGame(message.game);
            System.out.println();
            System.out.println(gamePlayClient.eval("redraw"));
        } else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION && notification.getMessage().contains("resign")) {
            System.out.println(gamePlayClient.eval("redraw"));
            System.out.println(SET_TEXT_COLOR_BLUE + notification.getMessage());
            printPrompt();
        } else {
            System.out.println(SET_TEXT_COLOR_BLUE + notification.getMessage());
            printPrompt();
        }
    }
}
