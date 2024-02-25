package records;

import java.util.UUID;
public class AuthData {
  String authToken;

  String username;

  public AuthData(String username) {
    this.username = username;
    this.authToken = UUID.randomUUID().toString();
  }

  public AuthData(String username, String authToken) {
    this.username = username;
    this.authToken = authToken;
  }

  public String getAuthToken() {
    return authToken;
  }

  public String getUsername() {
    return username;
  }
}

