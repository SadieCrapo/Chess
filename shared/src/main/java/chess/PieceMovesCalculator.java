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
//    private boolean topEdge;
//    private boolean bottomEdge;
//    private boolean rightEdge;
//    private boolean leftEdge;
//    private boolean topMargin;
//    private boolean bottomMargin;
//    private boolean rightMargin;
//    private boolean leftMargin;
//    private boolean bottomLeft;
//    private boolean bottomRight;
//    private boolean topLeft;
//    private boolean topRight;

    public KnightMovesCalculator(ChessBoard board, ChessPosition startPos) {
        super(board, startPos);
//        if (this.row <= 2) {
//            bottomEdge = true;
//            if (this.row == 2) {
//                bottomMargin = true;
//            }
//        }
//        if (this.row >= 7) {
//            topEdge = true;
//            if (this.row == 7) {
//                topMargin = true;
//            }
//        }
//        if (this.col <= 2) {
//            leftEdge = true;
//            if (this.col == 2) {
//                leftMargin = true;
//            }
//        }
//        if (this.col >= 7) {
//            rightEdge = true;
//            if (this.col == 7) {
//                rightMargin = true;
//            }
//        }
//        if (this.row == 1 && this.col == 1) {
//            bottomLeft = true;
//        } else if (this.row == 1 && this.col == 8) {
//            bottomRight = true;
//        } else if (this.row == 8 && this.col == 1) {
//            topLeft = true;
//        } else if (this.row == 8 && this.col == 8) {
//            topRight = true;
//        }
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessPosition> positions = new ArrayList<>();

//        if (this.bottomLeft) {
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//        } else if (this.bottomRight) {
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//        } else if (this.topLeft) {
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//        } else if (this.topRight) {
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (this.bottomMargin) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//        } else if (this.bottomEdge) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//        } else if (this.topMargin) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (this.topEdge) {
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (this.leftMargin) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (this.leftEdge) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//        } else if (this.rightMargin) {
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (this.rightEdge) {
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        } else if (!(this.bottomEdge || this.topEdge || this.leftEdge || this.rightEdge)) {
//            positions.add(new ChessPosition(this.row+1, this.col+2));
//            positions.add(new ChessPosition(this.row-1, this.col+2));
//            positions.add(new ChessPosition(this.row+1, this.col-2));
//            positions.add(new ChessPosition(this.row-1, this.col-2));
//            positions.add(new ChessPosition(this.row+2, this.col+1));
//            positions.add(new ChessPosition(this.row-2, this.col+1));
//            positions.add(new ChessPosition(this.row+2, this.col-1));
//            positions.add(new ChessPosition(this.row-2, this.col-1));
//        }

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
//
//
//        int[] rows;
//        int[] columns;
//
//        rows = switch (this.row) {
//            case 1 -> new int[]{1, 2};
//            case 8 -> new int[]{7, 8};
//            default -> new int[]{this.row - 1, this.row, this.row + 1};
//        };
//        columns = switch (this.col) {
//            case 1 -> new int[]{1, 2};
//            case 8 -> new int[]{7, 8};
//            default -> new int[]{this.col - 1, this.col, this.col + 1};
//        };
//
//        for (int i : rows) {
//            for (int j : columns) {
//                ChessPosition endPos = new ChessPosition(i, j);
//                ChessPiece newPiece = this.board.getPiece(endPos);
//
//                if (newPiece == null) {
//                    this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                } else {
//                    if (newPiece.getTeamColor() != this.piece.getTeamColor()) {
//                        this.validMoves.add(new ChessMove(this.startPos, endPos, null));
//                    }
//                }
//            }
//        }
        return this.validMoves;
    }
}