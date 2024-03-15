package results;

import records.AuthData;
import records.UserData;

public class RegisterResult {
  String username;
  String authToken;
  String message;

  public RegisterResult(UserData user, AuthData auth) {
    this.username = user.getUsername();
    this.authToken = auth.getAuthToken();
  }

  public String getAuthToken() {
    return authToken;
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