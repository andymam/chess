package dataaccess;

import records.*;
import java.util.Collection;

public interface AuthDAO {

  void clearAuths() throws DataAccessException;

  AuthData getAuth(String authToken) throws DataAccessException;
  void addAuth(AuthData authToken) throws DataAccessException;
  void deleteAuthorization(String authToken) throws DataAccessException;

  boolean inAuths(String authToken) throws  DataAccessException;

}
