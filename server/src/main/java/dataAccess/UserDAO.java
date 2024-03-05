package dataAccess;

import records.*;

public interface UserDAO {
  void clearUsers() throws DataAccessException;
  void addUser(UserData user) throws DataAccessException;
  UserData getUser(String username, String password) throws DataAccessException;

}
