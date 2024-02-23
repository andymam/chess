package dataaccess;

import records.*;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;

import java.util.Collection;

public interface UserDAO {
  void clearUsers() throws DataAccessException;
  UserData addUser(UserData user) throws DataAccessException;
  UserData getUser(RegisterRequest registerRequest) throws DataAccessException;
  UserData getUser(LoginRequest loginRequest) throws DataAccessException;
  Collection<UserData> getUsers() throws DataAccessException;

}
