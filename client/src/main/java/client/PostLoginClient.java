package client;

import exception.BadRequestException;
import repl.REPL;
import request.CreateRequest;
import request.ListRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.ListResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.Arrays;

public class PostLoginClient implements Client {
    private final String serverUrl;
    private ServerFacade server;
    final REPL repl;
    private String authToken = "";

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
//        if (params.length >= 2) {
//            var username = params[0];
//            var password = params[1];

//        return String.format("AuthToken is: %s", authToken);

        server.logout(authToken);
//        LogoutResult result = server.logout(authToken);
        repl.setClientToPreLogin();

        return String.format("Successfully logged out user");
//        }
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
        return String.format("Current games: %s", result.games().toString());
    }

    public String join(String... params) {
        return "";
    }

    public String observe(String... params) {
        return "";
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
