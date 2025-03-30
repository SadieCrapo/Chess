package client;

import exception.BadRequestException;
import model.GameData;
import repl.REPL;
import request.*;
import result.*;
import ui.BoardPrinter.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static ui.BoardPrinter.printBoard;
import static ui.EscapeSequences.ERASE_SCREEN;

public class PostLoginClient implements Client {
    private final String serverUrl;
    private ServerFacade server;
    final REPL repl;
    private String authToken = "";
    ArrayList<GameData> gameList;

    public PostLoginClient(String serverUrl, ServerFacade server, REPL repl) {
        this.serverUrl = serverUrl;
        this.server = server;
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> quit();
                default -> help();
            };
//        } catch (BadRequestException ex) {
//            return ex.getMessage();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws BadRequestException {

        server.logout(authToken);
//        LogoutResult result = server.logout(authToken);
        repl.setClientToPreLogin();

        return String.format("Successfully logged out user");
    }

    public String create(String... params) throws BadRequestException {
        if (params.length >= 1) {
            var gameName = params[0];

            CreateResult result = server.create(new CreateRequest(gameName), authToken);

            return String.format("Successfully created game: %d", result.gameID());
        }
        throw new BadRequestException("Expected: <name>");
    }

    public String list() throws BadRequestException {
        ListResult result = server.list(authToken);

        gameList = result.games();

        if (gameList.isEmpty()) {
            return "No current games";
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        printWriter.println("Current games:");

        for (int i=0; i< gameList.size(); i++) {
            GameData game = gameList.get(i);
            printWriter.println(String.format("%d: %s black player: %s, white player %s", i+1, game.gameName(), game.blackUsername(), game.whiteUsername()));
        }

        printWriter.flush();
        return stringWriter.toString();
    }

    public String join(String... params) throws BadRequestException {
        if (params.length >= 2) {
            int listID = Integer.parseInt(params[0]);
            var teamColor = params[1].toUpperCase();

            int gameID = gameList.get(listID-1).gameID();

            JoinResult result = server.join(new JoinRequest(teamColor, gameID), authToken);

            return printBoard(teamColor, result.game());
        }
        throw new BadRequestException("Expected: <ID> <black/white>");
    }

    public String observe(String... params) throws BadRequestException {
        if (params.length >= 1) {
            int gameID = Integer.parseInt(params[0])-1;

//            server.observe(authToken);

            return printBoard("WHITE", gameList.get(gameID));
        }
        throw new BadRequestException("Expected: <ID>");
    }

    public String quit(String... params) throws BadRequestException {
        logout();
        return "quit";
    }

    @Override
    public String help() {
        return """
                Options:
                create <name> - create a new game
                list - view current games
                join <ID> <black/white> - play chess as specified color
                observe <ID> - watch specified game
                logout - to return to start
                quit - logout and exit chess""";
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
