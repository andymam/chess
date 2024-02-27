package server.results;

public class JoinGameResult {
  String message;

  public JoinGameResult(String message) {
    this.message = message;
  }

  public JoinGameResult() {}

  public String getMessage() {
    return message;
  }
}
