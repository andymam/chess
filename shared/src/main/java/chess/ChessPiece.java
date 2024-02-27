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

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

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
            case KING -> addKingMoves(board, myPosition, validMoves);
            case QUEEN -> addQueenMoves(board, myPosition, validMoves);
            case BISHOP -> addBishopMoves(board, myPosition, validMoves);
            case KNIGHT -> addKnightMoves(board, myPosition, validMoves);
            case ROOK -> addRookMoves(board, myPosition, validMoves);
            case PAWN -> addPawnMoves(board, myPosition, validMoves);
            default -> throw new UnsupportedOperationException("Not a piece");
        }
        return validMoves;
    }


    private void addKingMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] directions = {-1, 0, 1};

        for (int rowMove : directions) {
            for (int colMove : directions) {

                int newRow = currentRow + rowMove;
                int newCol = currentCol + colMove;

                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (onBoard(newRow, newCol) && (board.getPiece(newPosition) == null || board.getPiece(newPosition).pieceColor != this.pieceColor)) {
                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private void addQueenMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow1 = position.getRow();
        int currentCol1 = position.getColumn();

        int[] directions1 = {-1, 0, 1};

        for (int rowMove : directions1) {
            for (int colMove : directions1) {

                int newRow=currentRow1 + rowMove;
                int newCol=currentCol1 + colMove;

                movePiece(rowMove, colMove, newRow, newCol, board, position, validMoves);
            }
        }
    }

    private void addBishopMoves(ChessBoard board,  ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow2 = position.getRow();
        int currentCol2 = position.getColumn();

        int[] directions2 = {-1, 1};

        for (int rowMove : directions2) {
            for (int colMove : directions2) {

                int newRow = currentRow2 + rowMove;
                int newCol = currentCol2 + colMove;

                movePiece(rowMove, colMove, newRow, newCol, board, position, validMoves);
            }
        }
    }

    private void addKnightMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow3 = position.getRow();
        int currentCol3 = position.getColumn();

        int[] offsets = {-1, 1, -2, 2};

        for (int rowMove : offsets) {
            for (int colMove : offsets) {
                if (Math.abs(rowMove) + Math.abs(colMove) != 3) {
                    continue;
                }

                int newRow = currentRow3 + rowMove;
                int newCol = currentCol3 + colMove;

                ChessPosition newPosition = new ChessPosition(newRow, newCol);

                if (onBoard(newRow, newCol) && (board.getPiece(newPosition) == null || this.pieceColor != board.getPiece(newPosition).pieceColor)) {
                    validMoves.add(new ChessMove(position, newPosition));
                }
            }
        }
    }

    private void addRookMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow4 = position.getRow();
        int currentCol4 = position.getColumn();

        int[] directions3 = {-1, 0, 1};

        for (int rowMove : directions3) {
            for (int colMove : directions3) {
                if (rowMove != 0 && colMove != 0) {
                    continue;
                }

                int newRow = currentRow4 + rowMove;
                int newCol = currentCol4 + colMove;

                movePiece(rowMove, colMove, newRow, newCol, board, position, validMoves);
            }
        }
    }

    public void movePiece(int rowMove, int colMove, int newRow, int newCol, ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
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

    private void addPawnMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int currentRow5 = position.getRow();
        int currentCol5 = position.getColumn();

        int direction = (this.pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Pawn moves forward
        int newRow = currentRow5 + direction;
        int newCol = currentCol5;

        ChessPosition newPosition = new ChessPosition(newRow, newCol);
        ChessPiece spot = board.getPiece(newPosition);

        if (onBoard(newRow, newCol) && spot == null) {
            if ((newRow == 8 && this.pieceColor == ChessGame.TeamColor.WHITE) || (newRow == 1 && this.pieceColor == ChessGame.TeamColor.BLACK)) {
                addPromotionMoves(position, newPosition, validMoves);
            } else {
                validMoves.add(new ChessMove(position, newPosition));
            }

            // Pawn double move on first move
            if ((currentRow5 == 2 && this.pieceColor == ChessGame.TeamColor.WHITE) || (currentRow5 == 7 && this.pieceColor == ChessGame.TeamColor.BLACK)) {
                newRow += direction;
                if (board.getPiece(new ChessPosition(newRow, newCol)) == null) {
                    validMoves.add(new ChessMove(position, new ChessPosition(newRow, newCol)));
                }
            }
        }

        // Pawn captures diagonally
        int[] captureCols = {currentCol5 - 1, currentCol5 + 1};
        for (int col : captureCols) {
            newRow = currentRow5 + direction;
            if (onBoard(newRow, col)) {
                ChessPiece capturedPiece = board.getPiece(new ChessPosition(newRow, col));
                if (capturedPiece != null && capturedPiece.pieceColor != this.pieceColor) {
                    if ((newRow == 8 && this.pieceColor == ChessGame.TeamColor.WHITE) || (newRow == 1 && this.pieceColor == ChessGame.TeamColor.BLACK)) {
                        addPromotionMoves(position, new ChessPosition(newRow, col), validMoves);
                    } else {
                        validMoves.add(new ChessMove(position, new ChessPosition(newRow, col)));
                    }
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition position, ChessPosition newPosition, Collection<ChessMove> validMoves) {
        // Add moves with different promotion pieces (e.g., queen, knight, rook, bishop)
        ChessPiece.PieceType[] promotionTypes = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        for (ChessPiece.PieceType promotionPiece : promotionTypes) {
            ChessMove promotionMove = new ChessMove(position, newPosition, promotionPiece);
            validMoves.add(promotionMove);
        }
    }

    private boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
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
}