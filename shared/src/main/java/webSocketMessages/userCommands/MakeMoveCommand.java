package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
  private final int gameID;
  private final ChessMove move;

  public MakeMoveCommand(String auth, int gameID, ChessMove move) {
    super(auth);
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
