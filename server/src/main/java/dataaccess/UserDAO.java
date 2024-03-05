package dataaccess;

import records.*;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;

import java.util.Collection;

public interface UserDAO {
  void clearUsers() throws DataAccessException;
  void addUser(UserData user) throws DataAccessException;
  UserData getUser(String username, String password) throws DataAccessException;

}
