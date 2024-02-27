package server.requests;

public class JoinGameRequest{

  String playerColor;
  Integer gameID;
  String authorization;

  public JoinGameRequest(Integer gameID) {
    this.gameID = gameID;
  }

  public Integer getGameID() {
    return gameID;
  }

  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }
  public String getAuthorization() {
    return authorization;
  }

  public String getPlayerColor() {
    return playerColor;
  }
}
