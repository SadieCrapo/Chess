package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    protected Collection<ChessMove> validMoves = new ArrayList<>();
    protected ChessPosition startPos;
    protected int row;
    protected int col;
    protected ChessPiece piece;
    protected ChessBoard board;

    public PieceMovesCalculator(ChessBoard board, ChessPosition startPos) {
        this.startPos = startPos;
        this.row = startPos.getRow();
        this.col = startPos.getColumn();
        this.piece = board.getPiece(startPos);
        this.board = board;
    }

    public Collection<ChessMove> pieceMoves() {
        return this.validMoves;
    }

    public boolean addNewPiece(int newRow, int newCol) {
        ChessPosition endPos = new ChessPosition(newRow, newCol);
        ChessPiece newPiece = this.board.getPiece(endPos);

        if (newPiece == null) {
            this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            return false;
        } else {
            if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            }
            return true;
        }
    }
}

class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        for (int i=this.row+1; i <= 8; i++) {
            if (addNewPiece(i, this.col)) {
                break;
            }
        }

        for (int i=this.row-1; i >= 1; i--) {
            if (addNewPiece(i, this.col)) {
                break;
            }
        }

        for (int i=this.col+1; i <= 8; i++) {
            if (addNewPiece(this.row, i)) {
                break;
            }
        }

        for (int i=this.col-1; i >= 1; i--) {
            if (addNewPiece(this.row, i)) {
                break;
            }
        }
        return this.validMoves;
    }
}

class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        int i;
        int j;
        for (i=this.row+1, j=this.col+1; i <= 8 && j <= 8; i++, j++) {
            if (addNewPiece(i, j)) {
                break;
            }
        }

        for (i=this.row-1, j=this.col+1; i >= 1 && j <= 8; i--, j++) {
            if (addNewPiece(i, j)) {
                break;
            }
        }

        for (i=this.row+1, j=this.col-1; i <= 8 && j >= 1; i++, j--) {
            if (addNewPiece(i, j)) {
                break;
            }
        }

        for (i=this.row-1, j=this.col-1; i >= 1 && j >= 1; i--, j--) {
            if (addNewPiece(i, j)) {
                break;
            }
        }
        return this.validMoves;
    }
}

class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        int[] rows;
        int[] columns;

        rows = switch (this.row) {
            case 1 -> new int[]{1, 2};
            case 8 -> new int[]{7, 8};
            default -> new int[]{this.row - 1, this.row, this.row + 1};
        };
        columns = switch (this.col) {
            case 1 -> new int[]{1, 2};
            case 8 -> new int[]{7, 8};
            default -> new int[]{this.col - 1, this.col, this.col + 1};
        };

        for (int i : rows) {
            for (int j : columns) {
                addNewPiece(i, j);
            }
        }
        return this.validMoves;
    }
}

class QueenMovesCalculator extends PieceMovesCalculator {

    public QueenMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        RookMovesCalculator rook = new RookMovesCalculator(this.board, this.startPos);
        BishopMovesCalculator bishop = new BishopMovesCalculator(this.board, this.startPos);
        this.validMoves = rook.pieceMoves();
        this.validMoves.addAll(bishop.pieceMoves());

        return this.validMoves;
    }
}

class KnightMovesCalculator extends PieceMovesCalculator {
    public KnightMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

        positions.add(new ChessPosition(this.row+1, this.col+2));
        positions.add(new ChessPosition(this.row-1, this.col+2));
        positions.add(new ChessPosition(this.row+1, this.col-2));
        positions.add(new ChessPosition(this.row-1, this.col-2));
        positions.add(new ChessPosition(this.row+2, this.col+1));
        positions.add(new ChessPosition(this.row-2, this.col+1));
        positions.add(new ChessPosition(this.row+2, this.col-1));
        positions.add(new ChessPosition(this.row-2, this.col-1));

        for (ChessPosition endPos : positions) {
            try {
                ChessPiece newPiece = this.board.getPiece(endPos);

                if (newPiece == null) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                } else {
                    if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                        this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                    }
                }
            } catch(ArrayIndexOutOfBoundsException e) {}
        }

        return this.validMoves;
    }
}

class PawnMovesCalculator extends PieceMovesCalculator {
    private boolean initialMove = false;
    private boolean leftEdge = false;
    private boolean rightEdge = false;
    private ChessGame.TeamColor team;
    private int incrementer;

    public PawnMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
        this.team = this.piece.getTeamColor();
        if (this.team == ChessGame.TeamColor.BLACK) {
            this.incrementer = -1;
        } else {
            this.incrementer = 1;
        }
        if (this.team == ChessGame.TeamColor.BLACK) {
            if (this.startPos.getRow() == 7) {
                this.initialMove = true;
            }
        } else if (this.startPos.getRow() == 2) {
            this.initialMove = true;
        }

        if (this.col == 1) {
            leftEdge = true;
        } else if (this.col == 8) {
            rightEdge = true;
        }
    }

    public Collection<ChessMove> pieceMoves() {
        if (this.initialMove) {
            ChessPosition endPos = new ChessPosition(this.row+(2*incrementer), this.col);
            ChessPiece newPiece = this.board.getPiece(endPos);
            if (this.board.getPiece(new ChessPosition(this.row+incrementer, this.col)) == null) {
                if (newPiece == null) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
            }
        }

        if (!rightEdge) {
            ChessPosition endPos = new ChessPosition(this.row + incrementer, this.col+1);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece != null && newPiece.getTeamColor() != this.piece.getTeamColor()) {
                if (this.team == ChessGame.TeamColor.BLACK && endPos.getRow() == 1) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
                } else if (this.team == ChessGame.TeamColor.WHITE && endPos.getRow() == 8) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
                } else {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }            }
        }
        if (!leftEdge) {
            ChessPosition endPos = new ChessPosition(this.row + incrementer, this.col-1);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece != null && newPiece.getTeamColor() != this.piece.getTeamColor()) {
                if (this.team == ChessGame.TeamColor.BLACK && endPos.getRow() == 1) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
                } else if (this.team == ChessGame.TeamColor.WHITE && endPos.getRow() == 8) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                    this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
                } else {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }            }
        }

        ChessPosition endPos = new ChessPosition(this.row+incrementer, this.col);
        ChessPiece newPiece = this.board.getPiece(endPos);

        if (newPiece == null) {
            if (this.team == ChessGame.TeamColor.BLACK && endPos.getRow() == 1) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
            } else if (this.team == ChessGame.TeamColor.WHITE && endPos.getRow() == 8) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.QUEEN));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.BISHOP));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.ROOK));
                this.validMoves.add(new ChessMove(this.startPos, endPos, ChessPiece.PieceType.KNIGHT));
            } else {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            }
        }
        return this.validMoves;
    }
}