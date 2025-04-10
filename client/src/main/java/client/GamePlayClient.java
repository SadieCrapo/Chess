package client;

import chess.*;
import exception.BadRequestException;
import exception.ResponseException;
import model.GameData;
import repl.REPL;
import request.JoinRequest;
import result.JoinResult;

import java.util.Arrays;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.PAWN;
import static ui.BoardPrinter.printBoard;

public class GamePlayClient implements Client {
    final REPL repl;
//    ChessGame.TeamColor teamColor;
    String teamColor;
    ChessGame game;
//    ChessBoard board;

//    public GamePlayClient(REPL repl, ChessGame.TeamColor teamColor, GameData game) {
    public GamePlayClient(REPL repl, String teamColor, ChessGame game) {
        this.repl = repl;
        this.teamColor = teamColor;
        this.game = game;
//        this.board = game.
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String redraw() {
        return printBoard(teamColor, game);
    }

    public String leave() {
        return "";
    }

    public String move(String... params) throws BadRequestException {
        if (params.length >= 2) {
            String stringStart = params[0].toLowerCase();
            String stringEnd = params[1].toLowerCase();

            ChessPosition start;
            ChessPosition end;

            try {
                start = parsePosition(stringStart);
                end = parsePosition(stringEnd);
            } catch (BadRequestException e) {
                throw e;
            }

            ChessPiece.PieceType promotion = null;

            if (needsPromotion(start, end)) {
//                promotion = getPromotion();
                promotion = KNIGHT;
            }
            try {
                game.makeMove(new ChessMove(start, end, promotion));
            } catch (InvalidMoveException e) {
                throw new BadRequestException("invalid move");
            }
        }
        throw new BadRequestException("Expected: <start position> <end position>");
    }

    private ChessPosition parsePosition(String pos) throws BadRequestException {
        String stringRow = pos.substring(0, 1);
        String stringCol = pos.substring(1);

        int row;
        int col;

        row = switch (stringRow) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new BadRequestException("row must be a letter a-h");
        };
        try {
            col = Integer.parseInt(stringCol);
        } catch (NumberFormatException e) {
            throw new BadRequestException("column must be represented in digits");
        }

        return new ChessPosition(row, col);
    }

    private boolean needsPromotion(ChessPosition start, ChessPosition end) {
        if (game.getBoard().getPiece(start).getPieceType() == PAWN) {
            int endRow = end.getRow();

            if (game.getBoard().getPiece(start).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (endRow == 8) {
                    return true;
                }
            } else if (endRow == 1) {
                return true;
            }
        }
        return false;
    }

    public String resign() {
        return "";
    }

    public String highlight(String... params) {
        return "";
    }

    public String quit() throws ResponseException {
//        logout();
        return "quit";
    }

    @Override
    public String help() {
        return """
                Options:
                redraw - redraw the game board
                leave - leave the game
                move <start position> <end position> - make a move: ex. <move b2 c2>
                resign - forfeit the game
                highlight <start position> - highlight all possible moves for a specified piece: ex. <highlight a6>
                help - view possible commands
                quit - exit the program""";    }
}
