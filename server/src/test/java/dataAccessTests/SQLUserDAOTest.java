package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.UserData;

public class SQLUserDAOTest {

  UserDAO userDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    userDAO = new SQLUserDAO();
    userDAO.clearUsers(); // Clear any existing users before each test
  }

  @Test
  @DisplayName("Add user to database")
  public void addUserToDatabase() throws DataAccessException {
    UserData user = new UserData("testUser", "password123", "test@example.com");
    userDAO.addUser(user);
    UserData retrievedUser = userDAO.getUser("testUser", "password123");
    Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
    Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
  }

  @Test
  @DisplayName("Retrieve user from database")
  public void retrieveUserFromDatabase() throws DataAccessException {
    UserData user = new UserData("testUser", "password123", "test@example.com");
    userDAO.addUser(user);
    UserData retrievedUser = userDAO.getUser("testUser", "password123");
    Assertions.assertNotNull(retrievedUser);
    Assertions.assertEquals(user.getUsername(), retrievedUser.getUsername());
    Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
  }

  @Test
  @DisplayName("Retrieve non-existent user returns null")
  public void retrieveNonExistentUser() throws DataAccessException {
    UserData retrievedUser = userDAO.getUser("nonExistentUser", "password123");
    Assertions.assertNull(retrievedUser);
  }

  @Test
  @DisplayName("Clear users from database")
  public void clearUsersFromDatabase() throws DataAccessException {
    UserData user = new UserData("testUser", "password123", "test@example.com");
    userDAO.addUser(user);
    userDAO.clearUsers();
    UserData retrievedUser = userDAO.getUser("testUser", "password123");
    Assertions.assertNull(retrievedUser);
  }
}
