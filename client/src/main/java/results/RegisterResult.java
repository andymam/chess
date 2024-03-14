package results;

public class RegisterResult {
  String username;
  String authToken;
  String message;

  public RegisterResult(UserData user, AuthData auth) {
    this.username = user.getUsername();
    this.authToken = auth.getAuthToken();
  }

  public RegisterResult(String message) {
    this.message = message;
  }

  public String getUsername() {
    return username;
  }

  public String getMessage() {
    return message;
  }
}