package ui;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

import chess.*;
import model.GameData;

public class BoardPrinter {
    static StringWriter stringWriter;
    static PrintWriter printWriter;

    static String boardColor;

    public static String printBoard(String playerColor, ChessGame game) {
        return printBoard(playerColor, game, false, null);
    }

    public static String printBoard(String playerColor, ChessGame game, boolean highlight, ChessPosition highlightPos) {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        boardColor = SET_BG_COLOR_GREEN;
        String[] alphaHeader;

        if (playerColor.equals("BLACK")) {
            alphaHeader = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
            printHeader(alphaHeader);
            printBlackBoard(game, highlight, highlightPos);
        } else {
            alphaHeader = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
            printHeader(alphaHeader);
            printWhiteBoard(game, highlight, highlightPos);
        }

        printHeader(alphaHeader);
//        printWriter.print(ERASE_LINE);

        printWriter.flush();
        return stringWriter.toString();
    }

    private static void printPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            printWriter.print(SET_TEXT_COLOR_BLACK);
            switch (piece.getPieceType()) {
                case PAWN -> printWriter.print(BLACK_PAWN);
                case ROOK -> printWriter.print(BLACK_ROOK);
                case BISHOP -> printWriter.print(BLACK_BISHOP);
                case KNIGHT -> printWriter.print(BLACK_KNIGHT);
                case KING -> printWriter.print(BLACK_KING);
                case QUEEN -> printWriter.print(BLACK_QUEEN);
            }
        } else {
            printWriter.print(SET_TEXT_COLOR_WHITE);
            switch (piece.getPieceType()) {
                case PAWN -> printWriter.print(WHITE_PAWN);
                case ROOK -> printWriter.print(WHITE_ROOK);
                case BISHOP -> printWriter.print(WHITE_BISHOP);
                case KNIGHT -> printWriter.print(WHITE_KNIGHT);
                case KING -> printWriter.print(WHITE_KING);
                case QUEEN -> printWriter.print(WHITE_QUEEN);
            }
        }
    }

    private static void setBoardColor() {
        boardColor = (boardColor == SET_BG_COLOR_GREEN) ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN;
        printWriter.print(boardColor);
    }

    private static void printHeader(String[] alphaHeader) {
        printWriter.print(SET_BG_COLOR_LIGHT_GREY);
        printWriter.print(EMPTY);
        printWriter.print(SET_TEXT_COLOR_BLACK);

        for (int i=0; i<8; i++) {
            printWriter.print(String.format(" %s ", alphaHeader[i]));
        }

        printWriter.print(EMPTY);
        printWriter.print(RESET_BG_COLOR);
    }

    private static void printColumn(int i) {
        printWriter.print(SET_BG_COLOR_LIGHT_GREY);
        printWriter.print(SET_TEXT_COLOR_BLACK);
        printWriter.print(String.format(" %s ", i));
    }

    private static void printSquare(ChessPosition pos, ChessBoard board) {
        setBoardColor();
//        ChessPosition pos = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(pos);
        if (piece != null) {
            printPiece(piece);
        } else {
            printWriter.print(EMPTY);
        }
    }

    private static void printHighlightedSquare(ChessPosition pos, ChessBoard board) {
        setBoardColor();
        printWriter.print(SET_BG_COLOR_MAGENTA);
//        ChessPosition pos = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(pos);
        if (piece != null) {
            printPiece(piece);
        } else {
            printWriter.print(EMPTY);
        }
    }

    private static void printBlackBoard(ChessGame game, boolean highlight, ChessPosition highlightPos) {
        Collection<ChessMove> validMoves = null;
        ChessBoard board = game.getBoard();
        if (highlight) {
            validMoves = game.validMoves(highlightPos);
        }
        printWriter.print("\n");
        for (int i=1; i<=8; i++) {
            printColumn(i);

            setBoardColor();

            for (int j=8; j>0; j--) {
                ChessPosition pos = new ChessPosition(i, j);
                if (highlight) {
                    if (validMoves.contains(new ChessMove(highlightPos, pos, null))) {
                        printHighlightedSquare(pos, board);
                    } else {
                        printSquare(pos, board);
                    }
                } else {
                    printSquare(pos, board);
                }
            }

            printColumn(i);

            printWriter.println(RESET_BG_COLOR);
        }
    }

    private static void printWhiteBoard(ChessGame game, boolean highlight, ChessPosition highlightPos) {
        Collection<ChessMove> validMoves = null;
        ChessBoard board = game.getBoard();
        if (highlight) {
            validMoves = game.validMoves(highlightPos);
        }
        printWriter.print("\n");
        for (int i=8; i>0; i--) {
            printColumn(i);

            setBoardColor();

            for (int j=1; j<=8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (highlight) {
                    if (validMoves.contains(new ChessMove(highlightPos, pos, null))) {
                        printHighlightedSquare(pos, board);
                    } else {
                        printSquare(pos, board);
                    }
                } else {
                    printSquare(pos, board);
                }
            }

            printColumn(i);

            printWriter.println(RESET_BG_COLOR);
        }
    }
}
