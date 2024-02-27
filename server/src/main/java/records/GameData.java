package records;

public class GameData {
  int gameID;
  String whiteUsername;
  String blackUsername;
  String gameName;

  public GameData(int gameID, String gameName) {
    this.gameID = gameID;
    this.gameName = gameName;
  }

  public int getGameID() {
    return gameID;
  }


  public void setBlackUser(String blackUsername) {
    this.blackUsername = blackUsername;
  }

  public void setWhiteUser(String whiteUsername) {
    this.whiteUsername = whiteUsername;
  }

  public String getBlackUsername() {
    return blackUsername;
  }

  public String getWhiteUsername() {
    return whiteUsername;
  }
}
