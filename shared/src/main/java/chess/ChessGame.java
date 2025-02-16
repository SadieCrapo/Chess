package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTeam;

    private boolean blackKingMoved;
    private boolean blackLeftRookMoved;
    private boolean blackRightRookMoved;
    private boolean whiteKingMoved;
    private boolean whiteLeftRookMoved;
    private boolean whiteRightRookMoved;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTeam = TeamColor.WHITE;

        blackKingMoved = false;
        blackLeftRookMoved = false;
        blackRightRookMoved = false;
        whiteKingMoved = false;
        whiteLeftRookMoved = false;
        whiteRightRookMoved = false;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> invalidMoves = new ArrayList<>();
        validMoves = piece.pieceMoves(board, startPosition);
        ChessBoard tempBoard;

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            validMoves.addAll(castlingHelper(piece));
        }

        for (ChessMove move : validMoves) {
            tempBoard = board.clone();
            tempBoard.addPiece(move.getEndPosition(), piece);
            tempBoard.addPiece(startPosition, null);

            if (checkHelper(piece.getTeamColor(), tempBoard)) {
                invalidMoves.add(move);
            }
        }
        validMoves.removeAll(invalidMoves);

        return validMoves;
    }

    public Collection<ChessMove> castlingHelper(ChessPiece piece) {
        Collection<ChessMove> validCastleMoves = new ArrayList<>();

        ChessBoard tempBoard;
        ChessPosition startPos;
        ChessPosition endPos;

        if (piece.getTeamColor() == TeamColor.BLACK) {
            if (!blackKingMoved && board.getPiece(new ChessPosition(8, 5)) == piece) {
                startPos = new ChessPosition(8, 5);
                if (!blackLeftRookMoved) {
                    endPos = new ChessPosition(8, 3);
                    if (board.getPiece(new ChessPosition(8, 4)) == null && board.getPiece(endPos) == null) {
                        tempBoard = board.clone();
                        tempBoard.addPiece(new ChessPosition(8, 4), piece);
                        tempBoard.addPiece(startPos, null);
                        if (!checkHelper(TeamColor.BLACK, tempBoard)) {
                            tempBoard.addPiece(endPos, piece);
                            tempBoard.addPiece(new ChessPosition(8, 4), null);
                            if (!checkHelper(TeamColor.BLACK, tempBoard)) {
                                validCastleMoves.add(new ChessMove(startPos, endPos, null));
                            }
                        }
                    }
                }
                if (!blackRightRookMoved) {
                    endPos = new ChessPosition(8, 7);
                    if (board.getPiece(new ChessPosition(8, 6)) == null && board.getPiece(endPos) == null) {
                        tempBoard = board.clone();
                        tempBoard.addPiece(new ChessPosition(8, 6), piece);
                        tempBoard.addPiece(startPos, null);
                        if (!checkHelper(TeamColor.BLACK, tempBoard)) {
                            tempBoard.addPiece(endPos, piece);
                            tempBoard.addPiece(new ChessPosition(8, 6), null);
                            if (!checkHelper(TeamColor.BLACK, tempBoard)) {
                                validCastleMoves.add(new ChessMove(startPos, endPos, null));
                            }
                        }
                    }
                }
            }
        } else {
            if (!whiteKingMoved && board.getPiece(new ChessPosition(1, 5)) == piece) {
                startPos = new ChessPosition(1, 5);
                if (!whiteLeftRookMoved) {
                    endPos = new ChessPosition(1, 3);
                    if (board.getPiece(new ChessPosition(1, 4)) == null && board.getPiece(endPos) == null) {
                        tempBoard = board.clone();
                        tempBoard.addPiece(new ChessPosition(1, 4), piece);
                        tempBoard.addPiece(startPos, null);
                        if (!checkHelper(TeamColor.WHITE, tempBoard)) {
                            tempBoard.addPiece(endPos, piece);
                            tempBoard.addPiece(new ChessPosition(1, 4), null);
                            if (!checkHelper(TeamColor.WHITE, tempBoard)) {
                                validCastleMoves.add(new ChessMove(startPos, endPos, null));
                            }
                        }
                    }
                }
                if (!whiteRightRookMoved) {
                    endPos = new ChessPosition(1, 7);
                    if (board.getPiece(new ChessPosition(1, 6)) == null && board.getPiece(endPos) == null) {
                        tempBoard = board.clone();
                        tempBoard.addPiece(new ChessPosition(1, 6), piece);
                        tempBoard.addPiece(startPos, null);
                        if (!checkHelper(TeamColor.WHITE, tempBoard)) {
                            tempBoard.addPiece(endPos, piece);
                            tempBoard.addPiece(new ChessPosition(1, 6), null);
                            if (!checkHelper(TeamColor.WHITE, tempBoard)) {
                                validCastleMoves.add(new ChessMove(startPos, endPos, null));
                            }
                        }
                    }
                }
            }
        }

        return validCastleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPos);

        if (piece == null) {
            throw new InvalidMoveException("piece is null");
        }
        if (piece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException(piece.getTeamColor().toString() + " went out of turn");
        }

        Collection<ChessMove> validMoves = validMoves(startPos);

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException(currentTeam + " made an invalid move");
        }

        board.addPiece(startPos, null);

        if (move.getPromotionPiece() != null) {
            board.addPiece(endPos, new ChessPiece(currentTeam, move.getPromotionPiece()));
        } else {
            board.addPiece(endPos, piece);
        }

        if (moveCastling(move, piece)) {
            if (currentTeam == TeamColor.BLACK) {
                blackKingMoved = true;
                blackRightRookMoved = true;
                blackLeftRookMoved = true;
            } else {
                whiteKingMoved = true;
                whiteRightRookMoved = true;
                whiteLeftRookMoved = true;
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (currentTeam == TeamColor.WHITE && !whiteKingMoved) {
                whiteKingMoved = true;
            } else if (currentTeam == TeamColor.BLACK && !blackKingMoved) {
                blackKingMoved = true;
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (currentTeam == TeamColor.WHITE) {
                if (startPos.equals(new ChessPosition(1, 1)) && !whiteLeftRookMoved) {
                    whiteLeftRookMoved = true;
                } else if (startPos.equals(new ChessPosition(1, 8)) && !whiteRightRookMoved) {
                    whiteRightRookMoved = true;
                }
            } else if (startPos.equals(new ChessPosition(8, 1)) && !blackLeftRookMoved) {
                blackLeftRookMoved = true;
            } else if (startPos.equals(new ChessPosition(8, 8)) && !blackRightRookMoved) {
                blackRightRookMoved = true;
            }
        }

        if (currentTeam == TeamColor.WHITE) {
            currentTeam = TeamColor.BLACK;
        } else {
            currentTeam = TeamColor.WHITE;
        }
    }

    private boolean moveCastling(ChessMove move, ChessPiece piece) {
        int row = move.getEndPosition().getRow();
        int col = move.getEndPosition().getColumn();

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (col+2 == move.getStartPosition().getColumn()) {
                board.addPiece(new ChessPosition(row, col+1), board.getPiece(new ChessPosition(row, 1)));
                board.addPiece(new ChessPosition(row, 1), null);
                return true;
            } else if (col-2 == move.getStartPosition().getColumn()) {
                board.addPiece(new ChessPosition(row, col-1), board.getPiece(new ChessPosition(row, 8)));
                board.addPiece(new ChessPosition(row, 8), null);
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return checkHelper(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        ChessBoard tempBoard;

        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessPosition pos;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                pos = new ChessPosition(i+1, j+1);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    validMoves.addAll(piece.pieceMoves(board, pos));
                }
            }
        }

        for (ChessMove move : validMoves) {
            tempBoard = board.clone();
            tempBoard.addPiece(move.getEndPosition(), tempBoard.getPiece(move.getStartPosition()));
            tempBoard.addPiece(move.getStartPosition(), null);

            if (!checkHelper(teamColor, tempBoard)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkHelper(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPos = board.findPiecePosition(ChessPiece.PieceType.KING, teamColor);

        ChessPosition otherPos;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                otherPos = new ChessPosition(i+1, j+1);
                ChessPiece other = board.getPiece(otherPos);
                if (other != null && other.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleMoves = other.pieceMoves(board, otherPos);
                    for (var move : possibleMoves) {
                        if (move.getEndPosition().equals(kingPos)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheckmate(teamColor)) {
            ChessPosition pos;
            for (int i=0; i<8; i++) {
                for (int j=0; j<8; j++) {
                    pos = new ChessPosition(i+1, j+1);
                    ChessPiece piece = board.getPiece(pos);
                    if (piece != null && piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> validMoves = validMoves(pos);
                        if (!validMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;

        this.blackKingMoved = false;
        this.blackLeftRookMoved = false;
        this.blackRightRookMoved = false;
        this.whiteKingMoved = false;
        this.whiteLeftRookMoved = false;
        this.whiteRightRookMoved = false;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
