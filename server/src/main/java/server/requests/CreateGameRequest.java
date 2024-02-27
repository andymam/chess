package server.requests;

public class CreateGameRequest {
  String gameName;

  String auth;

  public CreateGameRequest(String name){
    this.gameName = name;
  }

  public void setAuth(String auth) {
    this.auth = auth;
  }

  public String getAuthorization() {
    return auth;
  }

  public String getGameName() {
    return gameName;
  }
}
