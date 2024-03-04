package dataaccess;

import records.UserData;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
  ArrayList<UserData> users = new ArrayList<>();

  public void clearUsers() {
    users.clear();
  }

  public void addUser(UserData user) throws DataAccessException {
    for (UserData youser : users) {
      if (Objects.equals(user.getUsername(), youser.getUsername())) {
        throw new DataAccessException("already taken");
      }
    }
    users.add(user);
  }

  public UserData getUser(String username) {
    for (UserData user : users) {
      if (Objects.equals(user.getUsername(), username)) {
        return user;
      }
    }
    return null;
  }
}
