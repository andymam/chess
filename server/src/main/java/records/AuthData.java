package records;

import java.util.UUID;
public class AuthData {
  String authToken;

  String username;

  public AuthData(String username) {
    this.username = username;
    this.authToken = UUID.randomUUID().toString();
  }

  public String getAuthToken() {
    return authToken;
  }
}
