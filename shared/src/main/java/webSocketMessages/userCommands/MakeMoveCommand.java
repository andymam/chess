package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
  private int gameID;
  private ChessMove move;

  public MakeMoveCommand(String auth, int gameID, ChessMove move, boolean highlightMoves) {
    super(auth);
    commandType = CommandType.MAKE_MOVE;
    this.gameID = gameID;
    this.move = move;
  }

  public int getGameID() {
    return gameID;
  }

  public ChessMove getMove() {
    return move;
  }
}
