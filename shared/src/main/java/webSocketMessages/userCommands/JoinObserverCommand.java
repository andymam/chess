package webSocketMessages.userCommands;


public class JoinObserverCommand extends UserGameCommand {
  private final int gameID;
  public JoinObserverCommand(String auth, int gameID) {
    super(auth);
    commandType = CommandType.JOIN_OBSERVER;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
