package webSocketMessages.userCommands;
public class LeaveCommand extends UserGameCommand {
  private final int gameID;

  public LeaveCommand(String auth, int gameID) {
    super(auth);
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
