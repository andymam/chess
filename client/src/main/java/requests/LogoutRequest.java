package requests;

public class LogoutRequest {
  String auth;

  public LogoutRequest(String auth) {
    this.auth = auth;
  }

  public String getAuth() {
    return auth;
  }
}
