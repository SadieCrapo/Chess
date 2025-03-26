package client;

import java.util.Arrays;

public class PreLoginClient implements Client {
//    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
//        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String input) {
//        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
//        } catch (ResponseException ex) {
//            return ex.getMessage();
//        }
    }

    public String login(String... params) {
        return "";
    }

    public String register(String... params) {
        return "";
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
