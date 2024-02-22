package dataaccess;

import records.*;
import java.util.Collection;

public interface AuthDAO {

  void clearAuths() throws DataAccessException;
  Collection<AuthData> getAuthTokens() throws DataAccessException;
  void addAuth(AuthData authToken) throws DataAccessException;
  boolean getAuthorization(String authToken) throws DataAccessException;
  void deleteAuthorization(String authToken) throws DataAccessException;

}
