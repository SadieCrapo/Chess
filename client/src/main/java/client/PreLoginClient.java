package client;

import exception.ResponseException;
import repl.REPL;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import exception.BadRequestException;

import java.util.Arrays;

public class PreLoginClient implements Client {
    private ServerFacade server;
    final REPL repl;

    public PreLoginClient(String serverUrl, ServerFacade server, REPL repl) {
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
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws BadRequestException, ResponseException {
        if (params.length >= 2) {
            var username = params[0];
            var password = params[1];

            LoginResult result;

            try {
                result = server.login(new LoginRequest(username, password));
            } catch (ResponseException e) {
                if (e.getStatus() == 401) {
                    return "username or password is incorrect";
                } else {
                    throw e;
                }
            }

            String list = repl.setClientToPostLogin(result.authToken());

            return String.format("Welcome %s!\n", result.username()) + list;
        }
        throw new BadRequestException("Expected: <username> <password>");
    }

    public String register(String... params) throws BadRequestException, ResponseException {
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            RegisterResult result;

            try {
                result = server.register(new RegisterRequest(username, password, email));
            } catch (ResponseException e) {
                if (e.getStatus() == 403) {
                    return "username has already been claimed";
                }
                throw e;
            }
            String list = repl.setClientToPostLogin(result.authToken());

            return String.format("Welcome %s!\n", result.username()) + list;
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
