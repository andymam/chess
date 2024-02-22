package records;

import java.util.UUID;
public class AuthData {
  String authToken;

  public AuthData() {
    this.authToken = UUID.randomUUID().toString();
  }

  public String getAuthToken() {
    return authToken;
  }
}
