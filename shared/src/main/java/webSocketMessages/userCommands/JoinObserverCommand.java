package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserverCommand extends UserGameCommand {
  private final int gameID;
  public JoinObserverCommand(String auth, int gameID) {
    super(auth);
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
