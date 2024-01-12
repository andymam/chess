package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        switch (type) {
            case KING:
                addKingMoves(board, myPosition, validMoves);
                break;
            case QUEEN:
                addQueenMoves(board, myPosition, validMoves);
                break;
            case BISHOP:
                addBishopMoves(board, myPosition, validMoves);
                break;
            case KNIGHT:

                break;
            case ROOK:

                break;
            case PAWN:

                break;
            default:
                throw new UnsupportedOperationException("Not a piece");
        }
        return validMoves;
    }

    private void addKingMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {-1, 0, 1};

        for (int rowMove : directions) {
            for (int colMove : directions) {
                if (rowMove == 0 && colMove == 0) {
                    continue;
                }

                int newRow = currentRow + rowMove;
                int newCol = currentCol + colMove;

                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (isValidPosition(newRow, newCol) && (board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != this.pieceColor)) {
                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private void addQueenMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {};

        for (int rowMove : directions) {
            for (int colMove : directions) {
                if (rowMove == 0 && colMove == 0) {
                    continue;
                }

                int newRow=currentRow + rowMove;
                int newCol=currentCol + colMove;

                if (isValidPosition(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private void addBishopMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {-1, 1};

        for (int rowMove : directions) {
            for (int colMove : directions) {
                if (rowMove == 0 && colMove == 0) {
                    continue;
                }

                int newRow=currentRow + rowMove;
                int newCol=currentCol + colMove;

                if (isValidPosition(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
