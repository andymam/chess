package records;

import chess.ChessGame;

public class GameData {
  int gameID;
  String whiteUsername;
  String blackUsername;
  String gameName;
  ChessGame game;

  public GameData(int gameID, String gameName) {
    this.gameID = gameID;
    this.gameName = gameName;
  }

  public int getGameID() {
    return gameID;
  }

  public String getGameName() {
    return gameName;
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
