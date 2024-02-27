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

  public UserData addUser(UserData user) {
    for (UserData youser : users) {
      if (Objects.equals(user.getUsername(), youser.getUsername())) {
        return null;
      }
    }
    users.add(user);
    return user;
  }

  public UserData getUser(String username, String password) {
    for (UserData user : users) {
      if (Objects.equals(user.getUsername(), username) && Objects.equals(password, user.getPassword())) {
        return user;
      }
    }
    return null;
  }
}
