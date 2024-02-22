package dataaccess;

import records.UserData;

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

  public UserData getUser(String username) {
    for (UserData user : users) {
      if (Objects.equals(user.getUsername(), username)) {
        return user;
      }
    }
    return null;
  }

  public Collection<UserData> getUsers() {
    return users;
  }
}
