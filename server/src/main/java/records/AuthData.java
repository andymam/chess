package records;

import java.util.UUID;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthData authToken = (AuthData) o;
    return Objects.equals(authToken, authToken.authToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authToken);
  }
}

