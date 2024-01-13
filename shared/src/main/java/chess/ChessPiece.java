package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
                addKnightMoves(board, myPosition, validMoves);
                break;
            case ROOK:
//                addRookMoves(board, myPosition, validMoves);
                break;
            case PAWN:
//                addPawnMoves(board, myPosition, validMoves);
                break;
            default:
                throw new UnsupportedOperationException("Not a piece");
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private void addKingMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
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

                if (!onBoard(newRow, newCol)) {
                    continue;
                }

                if (onBoard(newRow, newCol) && (board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != this.pieceColor)) {
                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private void addQueenMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {-1, 0, 1};

        for (int rowMove : directions) {
            for (int colMove : directions) {
                if (rowMove == 0 && colMove == 0) {
                    continue;
                }

                int newRow=currentRow + rowMove;
                int newCol=currentCol + colMove;

                while (onBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece spot = board.getPiece(newPosition);

                    if (spot == null) {
                        validMoves.add(new ChessMove(position, newPosition));
                    } else if (spot.pieceColor != this.pieceColor) {
                        validMoves.add(new ChessMove(position, newPosition));
                        break;
                    } else {
                        break;
                    }

                newRow += rowMove;
                newCol += colMove;
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

                int newRow = currentRow + rowMove;
                int newCol = currentCol + colMove;

                while (onBoard(newRow, newCol)) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece spot = board.getPiece(newPosition);

                    if (spot == null) {
                        validMoves.add(new ChessMove(position, newPosition));
                    } else if (spot.pieceColor != this.pieceColor) {
                        validMoves.add(new ChessMove(position, newPosition));
                        break;
                    } else {
                        break;
                    }

                    newRow += rowMove;
                    newCol += colMove;
                }
            }
        }
    }

    private void addKnightMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {-1, 1, -2, 2};

        for (int rowMove : directions) {
            for (int colMove : directions) {
                if (rowMove == 0 && colMove == 0) {
                    continue;
                }

                int newRow = currentRow + rowMove;
                int newCol = currentCol + colMove;

                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece spot = board.getPiece(newPosition);

                if (onBoard(newRow, newCol) && (spot == null || this.pieceColor != spot.pieceColor)) {
                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

//    private void addRookMoves() {
//
//    }
//
//    private void addPawnMoves() {
//
//    }

    private boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
