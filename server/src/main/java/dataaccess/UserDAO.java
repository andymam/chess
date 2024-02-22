package dataaccess;

import records.*;
import java.util.Collection;

public interface UserDAO {
  void clearUsers() throws DataAccessException;
  UserData addUser(UserData user) throws DataAccessException;
  UserData getUser(String username) throws DataAccessException;
  Collection<UserData> getUsers() throws DataAccessException;

}
