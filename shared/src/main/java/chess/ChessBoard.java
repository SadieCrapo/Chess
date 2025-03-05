package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;

        return board[row][col];
    }

    public ChessPosition findPiecePosition(ChessPiece.PieceType pieceType, ChessGame.TeamColor team) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                ChessPiece piece = board[i][j];
                if (piece != null && (piece.getPieceType() == pieceType && piece.getTeamColor() == team)) {
                    return new ChessPosition(i+1, j+1);
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        for (int i = 0; i < 8; i++) {
            switch (i) {
                case 0, 7:
                    board[i] = new ChessPiece[]{new ChessPiece(color, ChessPiece.PieceType.ROOK), new ChessPiece(color, ChessPiece.PieceType.KNIGHT), new ChessPiece(color, ChessPiece.PieceType.BISHOP), new ChessPiece(color, ChessPiece.PieceType.QUEEN), new ChessPiece(color, ChessPiece.PieceType.KING), new ChessPiece(color, ChessPiece.PieceType.BISHOP), new ChessPiece(color, ChessPiece.PieceType.KNIGHT), new ChessPiece(color, ChessPiece.PieceType.ROOK)};
                    break;
                case 1:
                    for (int j = 0; j < 8; j++) {
                        board[i][j] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                    }
                    break;
                case 6:
                    color = ChessGame.TeamColor.BLACK;
                    for (int j = 0; j < 8; j++) {
                        board[i][j] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                    }
                    break;
                default:
                    for (int j = 0; j < 8; j++) {
                        board[i][j] = null;
                    }
                    break;
            }
        }
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard other = (ChessBoard) o;
        ChessPosition pos;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                pos = new ChessPosition(i+1, j+1);
                if (getPiece(pos) == null || other.getPiece(pos) == null) {
                    if (!(getPiece(pos) == other.getPiece(pos))) {
                        return false;
                    }
                } else if (!(getPiece(pos).equals(other.getPiece(pos)))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ChessPosition pos;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                pos = new ChessPosition(i+1, j+1);
                if (getPiece(pos) == null) {
                    result.append("_");
                } else if (getPiece(pos).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    result.append(getPiece(pos).getPieceType().toString().substring(0,1).toLowerCase());
                } else {
                    result.append(getPiece(pos).getPieceType().toString().substring(0,1).toUpperCase());
                }
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public ChessBoard clone() {
        try {
            ChessBoard clone = (ChessBoard) super.clone();

            ChessPiece[][] cloneBoard = new ChessPiece[8][8];
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    cloneBoard[i][j] = board[i][j];
                }
            }
            clone.board = cloneBoard;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}