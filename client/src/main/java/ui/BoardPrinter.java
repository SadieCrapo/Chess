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

        printHeader();

        for (int i=8; i>0; i--) {
            ChessBoard board = game.getBoard();
            printWriter.print(SET_BG_COLOR_LIGHT_GREY);
            printWriter.print(String.format(" %s ", i));

            setBoardColor();

            for (int j=8; j>0; j--) {
                setBoardColor();
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null) {
                    printPiece(piece);
                } else {
                    printWriter.print(EMPTY);
                }
            }

            printWriter.print(SET_BG_COLOR_LIGHT_GREY);
            printWriter.print(String.format(" %s ", i));

            printWriter.println(RESET_BG_COLOR);
        }

        printHeader();

        printWriter.flush();
        return stringWriter.toString();
    }

    private static void printPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            switch (piece.getPieceType()) {
                case PAWN -> printWriter.print(BLACK_PAWN);
                case ROOK -> printWriter.print(BLACK_ROOK);
                case BISHOP -> printWriter.print(BLACK_BISHOP);
                case KNIGHT -> printWriter.print(BLACK_KNIGHT);
                case KING -> printWriter.print(BLACK_KING);
                case QUEEN -> printWriter.print(BLACK_QUEEN);
            }
        } else {
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

    private static void printHeader() {
        String[] alphaHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};

        printWriter.print(SET_BG_COLOR_LIGHT_GREY);
        printWriter.print(EMPTY);
        printWriter.print(SET_TEXT_COLOR_BLACK);

        for (int i=0; i<8; i++) {
            printWriter.print(String.format(" %s ", alphaHeader[i]));
        }

        printWriter.print(EMPTY);
        printWriter.println(RESET_BG_COLOR);
    }
}
