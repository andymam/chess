package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private boolean finishedGame = false;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void finishGame() {
        finishedGame = true;
    }

    public boolean gameOver() {
        return finishedGame;
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
        Set<ChessMove> validMoves = new HashSet<>(piece.pieceMoves(board, startPosition));
        removeIfInCheck(validMoves);
        return validMoves;
    }

    private void removeIfInCheck(Set<ChessMove> validMoves) {
        validMoves.removeIf(move -> {
            ChessPiece startPiece = board.getPiece(move.getStartPosition());
            ChessPiece endPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), startPiece);
            board.addPiece(move.getStartPosition(), null);
            boolean isInCheck = isInCheck(startPiece.getTeamColor());
            board.addPiece(move.getStartPosition(), startPiece);
            board.addPiece(move.getEndPosition(), endPiece);
            return isInCheck;
        });
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if the move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        // Get the piece at the starting position
        ChessPiece startPiece = board.getPiece(startPosition);

        // Check if there's a piece at the starting position
        if (startPiece == null) {
            throw new InvalidMoveException("No piece at the starting position");
        }

        // Check if it's the correct team's turn to move
        if (startPiece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not the current team's turn to move");
        }

        // Get the set of valid moves for the piece at the starting position
        Set<ChessMove> validMoves = new HashSet<>(startPiece.pieceMoves(board, startPosition));

        // Check if the specified move is in the set of valid moves
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        // Make the move on the board
        ChessPiece endPiece = board.getPiece(endPosition);
        board.addPiece(endPosition, startPiece);
        board.addPiece(startPosition, null);

        // Check for pawn promotion
        if (startPiece.getPieceType() == ChessPiece.PieceType.PAWN &&
                (endPosition.getRow() == 1 || endPosition.getRow() == 8)) {
            ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
            ChessPiece promotedPiece = new ChessPiece(teamTurn, promotionPiece);
            board.addPiece(endPosition, promotedPiece);
        }

        // Check if the move puts the current team's king in check
        if (isInCheck(teamTurn)) {
            // Undo the move if it puts the king in check
            board.addPiece(startPosition, startPiece);
            board.addPiece(endPosition, endPiece);
            throw new InvalidMoveException("Move puts the king in check");
        }

        // Switch the team's turn
        switchTeamTurn();
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);

        for (int row = 1; row <= 8; row ++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    continue;
                }
                if (!(piece.getTeamColor() == teamColor)) {
                    Set<ChessMove> validMoves = new HashSet<>(piece.pieceMoves(board, position));
                    for (ChessMove move : validMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (moveAvailable(teamColor)) {
            return false;
        }
        return isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (moveAvailable(teamColor)) {
            return false;
        }
        return !isInCheck(teamColor);
//        return !moveAvailable(teamColor);
    }

    private boolean moveAvailable(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                if (piece == null) {
                    continue;
                }

                if (piece.getTeamColor().equals(teamColor)) {
                    Set<ChessMove> validMoves = new HashSet<>(validMoves(position));

                    if (!validMoves.isEmpty()) {
                        return true;
                    }
                }
            }
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
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private void switchTeamTurn() {
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null) {
                    continue;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor().equals(teamColor)) {
                    return position;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame=(ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
