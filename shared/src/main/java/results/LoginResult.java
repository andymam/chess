package results;

import records.AuthData;
import records.UserData;

public class LoginResult {
  String username;
  String authToken;
  String message;
  public LoginResult(UserData user, AuthData authToken) {
    this.username = user.getUsername();
    this.authToken = authToken.getAuthToken();
  }

  public LoginResult(String message) {
    this.message = message;
  }

  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }

  public String getMessage() {
    return message;
  }

}