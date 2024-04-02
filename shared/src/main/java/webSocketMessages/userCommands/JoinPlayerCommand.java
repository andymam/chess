package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
  private final int gameID;
  private final ChessGame.TeamColor playerColor;
  public JoinPlayerCommand(String auth, int gameID, ChessGame.TeamColor playerColor) {
    super(auth);
    this.gameID = gameID;
    this.playerColor = playerColor;
  }

  public int getGameID() {
    return gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }
}
