package client;

import repl.REPL;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import exception.BadRequestException;

import java.util.Arrays;

public class PreLoginClient implements Client {
    private final String serverUrl;
    private ServerFacade server;
    final REPL repl;

    public PreLoginClient(String serverUrl, ServerFacade server, REPL repl) {
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
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (BadRequestException ex) {
            return ex.getMessage();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws BadRequestException {
        if (params.length >= 2) {
            var username = params[0];
            var password = params[1];

            LoginResult result = server.login(new LoginRequest(username, password));
            repl.setClientToPostLogin(result.authToken());

            return String.format("Successfully logged in user: %s with authToken: %s", result.username(), result.authToken());
        }
        throw new BadRequestException("Expected: <username> <password>");
    }

    public String register(String... params) throws BadRequestException {
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            RegisterResult result = server.register(new RegisterRequest(username, password, email));
            repl.setClientToPostLogin(result.authToken());

            return String.format("Successfully registered user: %s", username);
        }
        throw new BadRequestException("Expected: <username> <password> <email>");
    }

    @Override
    public String help() {
        return """
                Options:
                login <username> <password> - to play chess
                register <username> <password> <email> - create an account
                help - view possible commands
                quit - exit the program""";
    }
}
