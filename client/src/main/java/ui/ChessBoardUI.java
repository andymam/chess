package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {


  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;

  public static void main(String[] args){
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    ChessPiece[][] board = new ChessGame().getBoard().getBoard();
    ChessPiece[][] flippedBoard = flipBoard(board);
    String[] ogHeaders = {"h", "g", "f", "e", "d", "c", "b", "a"};
    String[] blackHeaders = {"a", "b", "c", "d", "e", "f", "g", "h"};
    String[] whiteNumbers = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String[] blackNumbers =  {"8", "7", "6", "5", "4", "3", "2", "1"};
    drawChessBoard(out, ogHeaders, whiteNumbers, board);
    out.println(SET_BG_COLOR_BLACK);
    drawChessBoard(out, blackHeaders, blackNumbers, flippedBoard);
  }

  public void printBoard() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    ChessPiece[][] board = new ChessGame().getBoard().getBoard();
    ChessPiece[][] flippedBoard = flipBoard(board);
    String[] ogHeaders = {"h", "g", "f", "e", "d", "c", "b", "a"};
    String[] blackHeaders = {"a", "b", "c", "d", "e", "f", "g", "h"};
    String[] whiteNumbers = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String[] blackNumbers =  {"8", "7", "6", "5", "4", "3", "2", "1"};
    drawChessBoard(out, ogHeaders, whiteNumbers, board);
    out.println(SET_BG_COLOR_BLACK);
    drawChessBoard(out, blackHeaders, blackNumbers, flippedBoard);
  }

  public static ChessPiece[][] flipBoard(ChessPiece[][] array) {
    int rows = array.length;
    int cols = array[0].length;
    ChessPiece[][] rotatedArray = new ChessPiece[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rotatedArray[i][j] = array[rows - 1 - i][cols - 1 - j];
      }
    }

    return rotatedArray;
  }

  public static void drawChessBoard(PrintStream out, String[] headers, String[] margin, ChessPiece[][] board){
    drawHeaders(out, headers);
    printRows(out, margin, board);
    drawHeaders(out, headers);
  }

  private static void printRows(PrintStream out, String[] margin, ChessPiece[][] board) {
    for (int row = 0; row <= board.length - 1; row++){
      out.print("                   ");
      printRow(out, row, margin, board[row]);
    }
  }

  private static void printRow(PrintStream out, int rowNum, String[] side, ChessPiece[] row) {
    out.print(SET_TEXT_COLOR_YELLOW);
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(" ");
    out.print(side[rowNum]);
    out.print(" ");
    for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++){
      if ((rowNum + i) % 2 == 0){
        out.print(SET_BG_COLOR_WHITE);
      }
      else{
        out.print(SET_BG_COLOR_BLACK);
      }
      out.print(" ");
      out.print(makePiece(row[i]));
      out.print(" ");
    }
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_YELLOW);
    out.print(" ");
    out.print(side[rowNum]);
    out.print(" ");
    out.print(SET_BG_COLOR_BLACK);
    out.println();
  }

  private static String makePiece(ChessPiece chessPiece) {
    if (chessPiece == null) {
      return " ";
    } else {
      ChessGame.TeamColor color = chessPiece.getTeamColor();
      ChessPiece.PieceType type = chessPiece.getPieceType();
      String t = switch (type) {
        case KING -> "♚";
        case QUEEN -> "♛";
        case BISHOP -> "♝";
        case KNIGHT -> "♞";
        case ROOK -> "♜";
        case PAWN -> "♟";
      };
      String c = switch (color) {
        case WHITE -> SET_TEXT_COLOR_BLUE;
        case BLACK -> SET_TEXT_COLOR_RED;
      };
      return c + t + RESET_TEXT_COLOR;
    }
  }

  private static void drawHeaders(PrintStream out, String[] headers) {
    out.print(SET_BG_COLOR_BLACK);
    out.print("                   ");
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(" ");
    out.print(" ");
    out.print(" ");
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawHeader(out, headers[boardCol]);
    }
    out.print(" ");
    out.print(" ");
    out.print(" ");
    out.print(SET_BG_COLOR_BLACK);
    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {
    out.print(" ".repeat(1));
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_YELLOW);
    out.print(headerText);
    out.print(" ".repeat(1));
  }
}
