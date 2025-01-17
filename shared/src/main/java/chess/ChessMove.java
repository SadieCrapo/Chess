package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (endPosition.getRow() == 8) {
            return this.promotionPiece;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChessMove)) {
            return false;
        }
        ChessMove move = (ChessMove) o;
        if (move.startPosition.equals(this.startPosition) && move.endPosition.equals(this.endPosition)) {
            if (move.promotionPiece == this.promotionPiece) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @return a 4 or 5 digit integer with the first as the starting row, second as the starting column,
     * third as the end row, fourth as the end column, and fifth as the promotional piece number
     */
    @Override
    public int hashCode() {
        String code;
        if (this.promotionPiece == null) {
            code = Integer.toString(this.startPosition.getRow()) + Integer.toString(this.startPosition.getColumn()) + Integer.toString(this.endPosition.getRow()) + Integer.toString(this.endPosition.getColumn());
        } else {
            code = Integer.toString(this.startPosition.getRow()) + Integer.toString(this.startPosition.getColumn()) + Integer.toString(this.endPosition.getRow()) + Integer.toString(this.endPosition.getColumn()) + this.promotionPiece.ordinal();
        }

        return Integer.parseInt(code);
    }
}
