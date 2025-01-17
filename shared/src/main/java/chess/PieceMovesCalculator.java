package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPos) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = startPos.getRow();
        int col = startPos.getColumn();
        ChessPiece thisPiece = board.getPiece(startPos);

        for (int i=row+1; i <= 8; i++) {
            ChessPosition endPos = new ChessPosition(i, col);
            ChessPiece newPiece = board.getPiece(endPos);

            if (newPiece == null) {
                validMoves.add(new ChessMove(startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != thisPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=row-1; i >= 1; i--) {
            ChessPosition endPos = new ChessPosition(i, col);
            ChessPiece newPiece = board.getPiece(endPos);

            if (newPiece == null) {
                validMoves.add(new ChessMove(startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != thisPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=col+1; i <= 8; i++) {
            ChessPosition endPos = new ChessPosition(row, i);
            ChessPiece newPiece = board.getPiece(endPos);

            if (newPiece == null) {
                validMoves.add(new ChessMove(startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != thisPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=col-1; i >= 1; i--) {
            ChessPosition endPos = new ChessPosition(row, i);
            ChessPiece newPiece = board.getPiece(endPos);

            if (newPiece == null) {
                validMoves.add(new ChessMove(startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != thisPiece.getTeamColor()) {
                    validMoves.add(new ChessMove(startPos, endPos, null));
                }
                break;
            }
        }
        return validMoves;
    }
}

//public class RookMovesCalculator extends PieceMovesCalculator {
//
//    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
//        return null;
//    }
//}
