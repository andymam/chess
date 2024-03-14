package records;

import chess.ChessGame;

public class GameData {
  int gameID;
  String whiteUsername;
  String blackUsername;
  String gameName;
  ChessGame chessGame;

  public GameData(int gameID, String gameName) {
    this.gameID = gameID;
    this.gameName = gameName;
    this.chessGame = new ChessGame();
  }

  public GameData(int gameID, String gameName, ChessGame game) {
    this.gameID = gameID;
    this.gameName = gameName;
    this.chessGame = game;
  }

  public void setGame(ChessGame game) {
    this.chessGame = game;
  }


  public int getGameID() {
    return gameID;
  }

  public String getGameName() {
    return gameName;
  }

  public ChessGame getGame() {
    return chessGame;
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
