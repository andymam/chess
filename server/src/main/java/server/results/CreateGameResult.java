package server.results;

public class CreateGameResult {
  Integer gameID;

  String message;

  public CreateGameResult(Integer id){
    this.gameID = id;
  }

  public CreateGameResult(String mess){
    this.message = mess;
  }

  public Integer getGameID() {
    return gameID;
  }

  public String getMessage() {
    return message;
  }
}
