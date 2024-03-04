package dataaccess;

import records.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
  ArrayList<AuthData> authTokens = new ArrayList<>();

  public void clearAuths() {
    authTokens.clear();
  }

  public void addAuth(AuthData authToken) {
    authTokens.add(authToken);
  }


  public void deleteAuthorization(String authToken)  {
    authTokens.removeIf(token -> Objects.equals(authToken, token.getAuthToken()));
  }

  public AuthData getAuth(String authToken) {
    for (AuthData auth : authTokens) {
      if (Objects.equals(auth.getAuthToken(), authToken)) {
        return auth;
      }
    }
    return null;
  }

  public boolean inAuths(String authToken) {
    for (AuthData auth : authTokens) {
      if (Objects.equals(auth.getAuthToken(), authToken)) {
        return true;
      }
    }
    return false;
  }

}