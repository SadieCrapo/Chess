package ui;

import java.io.StringWriter;
import java.io.PrintWriter;

import static ui.EscapeSequences.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

public class BoardPrinter {
    static StringWriter stringWriter;
    static PrintWriter printWriter;

    static String boardColor;

    public static String printBoard(String playerColor, GameData gameData) {
        ChessGame game = gameData.game();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        boardColor = SET_BG_COLOR_GREEN;
        String[] alphaHeader;

        if (playerColor.equals("BLACK")) {
            alphaHeader = new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};
            printHeader(alphaHeader);
            printBlackBoard(game.getBoard());
        } else {
            alphaHeader = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
            printHeader(alphaHeader);
            printWhiteBoard(game.getBoard());
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

    private static void printSquare(int i, int j, ChessBoard board) {
        setBoardColor();
        ChessPosition pos = new ChessPosition(i, j);
        ChessPiece piece = board.getPiece(pos);
        if (piece != null) {
            printPiece(piece);
        } else {
            printWriter.print(EMPTY);
        }
    }

    private static void printBlackBoard(ChessBoard board) {
        printWriter.print("\n");
        for (int i=1; i<=8; i++) {
            printColumn(i);

            setBoardColor();

            for (int j=8; j>0; j--) {
                printSquare(i, j, board);
            }

            printColumn(i);

            printWriter.println(RESET_BG_COLOR);
        }
    }

    private static void printWhiteBoard(ChessBoard board) {
        printWriter.print("\n");
        for (int i=8; i>0; i--) {
            printColumn(i);

            setBoardColor();

            for (int j=1; j<=8; j++) {
                printSquare(i, j, board);
            }

            printColumn(i);

            printWriter.println(RESET_BG_COLOR);
        }
    }
}
