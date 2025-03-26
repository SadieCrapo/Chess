package client;

import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import exception.BadRequestException;

import java.util.Arrays;

public class PreLoginClient implements Client {
    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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

            return String.format("Successfully logged in user: %s with authToken: %s", result.username(), result.authToken());
        }
        throw new BadRequestException("Expected: <username> <password>");
    }

    public String register(String... params) throws BadRequestException {
//        assertSignedIn();
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];

            RegisterResult result = server.register(new RegisterRequest(username, password, email));
//            var pet = new Pet(0, name, type);
//            pet = server.addPet(pet);
//            return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
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
                quit - exit the program
                """;
    }
}
