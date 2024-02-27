package server.requests;

public class LoginRequest {
  String username;
  String password;

  public LoginRequest(String username, String password) {
    this.username=username;
    this.password=password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
