package webSocketMessages.userCommands;
public class LeaveCommand extends UserGameCommand {
  private int gameID;

  public LeaveCommand(String auth, int gameID) {
    super(auth);
    commandType = CommandType.LEAVE;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
}
