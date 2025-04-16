package client;

import chess.*;
import client.websocket.WebSocketFacade;
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
    private ServerFacade server;
    private WebSocketFacade ws;
    final REPL repl;
    String teamColor;
    ChessGame game;
    int gameID;
    ChessBoard board;
    boolean observer;
    String authToken;

//    public GamePlayClient(REPL repl, ChessGame.TeamColor teamColor, GameData game) {
    public GamePlayClient(ServerFacade server, REPL repl) {
        this.server = server;
        this.ws = null;
        this.repl = repl;
        this.teamColor = "";
        this.game = null;
        this.gameID = -1;
        this.board = null;
        this.observer = false;
        this.authToken = "";
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

    public String leave() throws ResponseException {
        ws.leave(authToken, gameID);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // good practice
        }
        ws = null;
        repl.setClientToPostLogin(authToken);
        return "you left the game";
    }

    public String move(String... params) throws BadRequestException, ResponseException {
        if (observer) {
            return "observer cannot make move";
        }

        if (params.length > 1) {
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
//            return start.toString() + " " + end.toString();

            ChessPiece.PieceType promotion = null;

//            if (needsPromotion(start, end)) {
////                promotion = getPromotion();
//                promotion = KNIGHT;
//            }
            ChessMove move = new ChessMove(start, end, promotion);
            ws.makeMove(authToken, gameID, move);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // good practice
            }
//            return String.format("you moved %s", move);
            return "";
        }
        throw new BadRequestException("Expected: <start position> <end position> " + params.length);
    }

    private ChessPosition parsePosition(String pos) throws BadRequestException {
        String stringRow = pos.substring(0, 1);
        String stringCol = pos.substring(1);

        int row;
        int col;

        col = switch (stringRow) {
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
            row = Integer.parseInt(stringCol);
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

    public String resign() throws ResponseException {
        ws.resign(authToken, gameID);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // good practice
        }
        return "";
    }

    public String highlight(String... params) throws BadRequestException {
        if (params.length >= 1) {
            String stringStart = params[0].toLowerCase();

            ChessPosition start;

            try {
                start = parsePosition(stringStart);
            } catch (BadRequestException e) {
                throw e;
            }

            return printBoard(teamColor, game, true, start);
        }

        throw new BadRequestException("Expected: <start position>");
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

    public void setObserver(boolean observer) {
        this.observer = observer;
    }

    public void setGame(ChessGame game) {
        this.game = game;
        this.board = game.getBoard();
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setTeamColor(String team) {
        this.teamColor = team;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setWs(WebSocketFacade ws) {
        this.ws = ws;
    }

    public void setAuth(String authToken) {
        this.authToken = authToken;
    }
}
