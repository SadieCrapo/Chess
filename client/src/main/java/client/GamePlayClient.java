package client;

import exception.BadRequestException;
import exception.ResponseException;
import repl.REPL;

import java.util.Arrays;

public class GamePlayClient implements Client {
    final REPL repl;

    public GamePlayClient(REPL repl) {
        this.repl = repl;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> register(params);
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



    @Override
    public String help() {
        return "";
    }
}
