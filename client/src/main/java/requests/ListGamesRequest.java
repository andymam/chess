package requests;

public class ListGamesRequest {
  String authorization;

  public ListGamesRequest(String authorization) {
    this.authorization = authorization;
  }

  public String getAuthorization() {
    return authorization;
  }

}
