package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
}

class RookMovesCalculator extends PieceMovesCalculator {

    public RookMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
    }

    public Collection<ChessMove> pieceMoves() {
        for (int i=this.row+1; i <= 8; i++) {
            ChessPosition endPos = new ChessPosition(i, this.col);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=this.row-1; i >= 1; i--) {
            ChessPosition endPos = new ChessPosition(i, this.col);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=this.col+1; i <= 8; i++) {
            ChessPosition endPos = new ChessPosition(this.row, i);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (int i=this.col-1; i >= 1; i--) {
            ChessPosition endPos = new ChessPosition(this.row, i);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
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
        int i = 0;
        int j = 0;
        for (i=this.row+1, j=this.col+1; i <= 8 && j <= 8; i++, j++) {
            ChessPosition endPos = new ChessPosition(i, j);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (i=this.row-1, j=this.col+1; i >= 1 && j <= 8; i--, j++) {
            ChessPosition endPos = new ChessPosition(i, j);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (i=this.row+1, j=this.col-1; i <= 8 && j >= 1; i++, j--) {
            ChessPosition endPos = new ChessPosition(i, j);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
                break;
            }
        }

        for (i=this.row-1, j=this.col-1; i >= 1 && j >= 1; i--, j--) {
            ChessPosition endPos = new ChessPosition(i, j);
            ChessPiece newPiece = this.board.getPiece(endPos);

            if (newPiece == null) {
                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
            } else {
                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                }
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
                ChessPosition endPos = new ChessPosition(i, j);
                ChessPiece newPiece = this.board.getPiece(endPos);

                if (newPiece == null) {
                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                } else {
                    if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
                        this.validMoves.add(new ChessMove(this.startPos, endPos, null));
                    }
                }
            }
        }
//        for (i=this.row+1, j=this.col+1; i <= 8 && j <= 8; i++, j++) {
//            ChessPosition endPos = new ChessPosition(i, j);
//            ChessPiece newPiece = this.board.getPiece(endPos);
//
//            if (newPiece == null) {
//                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//            } else {
//                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
//                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                }
//            }
//        }
//
//        for (i=this.row-1, j=this.col+1; i >= 1 && j <= 8; i--, j++) {
//            ChessPosition endPos = new ChessPosition(i, j);
//            ChessPiece newPiece = this.board.getPiece(endPos);
//
//            if (newPiece == null) {
//                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//            } else {
//                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
//                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                }
//                break;
//            }
//        }
//
//        for (i=this.row+1, j=this.col-1; i <= 8 && j >= 1; i++, j--) {
//            ChessPosition endPos = new ChessPosition(i, j);
//            ChessPiece newPiece = this.board.getPiece(endPos);
//
//            if (newPiece == null) {
//                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//            } else {
//                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
//                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                }
//                break;
//            }
//        }
//
//        for (i=this.row-1, j=this.col-1; i >= 1 && j >= 1; i--, j--) {
//            ChessPosition endPos = new ChessPosition(i, j);
//            ChessPiece newPiece = this.board.getPiece(endPos);
//
//            if (newPiece == null) {
//                this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//            } else {
//                if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
//                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                }
//                break;
//            }
//        }
        return this.validMoves;
    }
}