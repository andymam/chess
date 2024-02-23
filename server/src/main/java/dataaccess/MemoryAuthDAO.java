package dataaccess;

import records.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {
  ArrayList<AuthData> authTokens = new ArrayList<>();

  public void clearAuths() {
    authTokens.clear();
  }

  public Collection<AuthData> getAuthTokens() {
    return authTokens;
  }

  public void addAuth(AuthData authToken) {
    authTokens.add(authToken);
  }

  public boolean getAuthorization(String authToken) {
    return authTokens.contains(authToken);
  }

  public void deleteAuthorization(String authToken)  {
    authTokens.remove(authToken);
  }

}