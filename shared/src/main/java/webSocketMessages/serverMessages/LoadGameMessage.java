package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
  private final ChessGame game;
  public LoadGameMessage(ChessGame game) {
    super(ServerMessageType.LOAD_GAME);
    this.game = game;
  }

  public ChessGame getChessGame() {
    return game;
  }
}
