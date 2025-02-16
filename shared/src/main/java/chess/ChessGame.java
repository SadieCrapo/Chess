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

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        currentTeam = TeamColor.WHITE;

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

        if (currentTeam == TeamColor.WHITE) {
            currentTeam = TeamColor.BLACK;
        } else {
            currentTeam = TeamColor.WHITE;
        }
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
//                        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, pos);
                    }
                }
            }
            return true;
        }
        return false;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
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
