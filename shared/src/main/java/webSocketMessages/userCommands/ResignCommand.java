package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
  private int gameID;
  public ResignCommand(String auth, int gameID) {
    super(auth);
    commandType = CommandType.RESIGN;
    this.gameID = gameID;
  }
  public int getGameID() {
    return gameID;
  }
}
