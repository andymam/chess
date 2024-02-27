package records;

import java.util.Objects;

public class UserData {
  String username;
  String password;
  String email;

  public UserData(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserData user = (UserData) o;
    return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email);
  }
}
