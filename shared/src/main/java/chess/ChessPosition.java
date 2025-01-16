package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChessPosition)) {
            return false;
        }
        ChessPosition pos = (ChessPosition) o;
        if (pos.row == this.row && pos.col == this.col) {
            return true;
        }

        return false;
    }

    /**
     *
     * @return a 2 digit integer with the first digit as the row number and the second as the column
     */
    @Override
    public int hashCode() {
        String code = Integer.toString(this.row) + Integer.toString(this.col);
        return Integer.parseInt(code);
    }
}
