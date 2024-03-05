package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;

public class SQLAuthDAOTest {

  AuthDAO authDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    authDAO = new SQLAuthDAO();
    authDAO.clearAuths(); // Clear any existing auth tokens before each test
  }

  @Test
  @DisplayName("Add auth token to database")
  public void addAuthTokenToDatabase() throws DataAccessException {
    AuthData authToken = new AuthData("testUser", "token123");
    authDAO.addAuth(authToken);
    AuthData retrievedAuthToken = authDAO.getAuth("token123");
    Assertions.assertEquals(authToken.getUsername(), retrievedAuthToken.getUsername());
  }

  @Test
  @DisplayName("Add auth token to database bad")
  public void addAuthTokenToDatabaseNegative() throws DataAccessException {
    try {
      AuthData authToken = new AuthData("testUser", "token123");
      authDAO.addAuth(authToken);
      AuthData authToken2 = new AuthData("testUser", "token1234");
      authDAO.addAuth(authToken2);
    }
    catch (DataAccessException e) {
      Assertions.assertEquals(e.getMessage(), "Error: bad request");
    }
  }

  @Test
  @DisplayName("Retrieve auth token from database")
  public void retrieveAuthTokenFromDatabase() throws DataAccessException {
    AuthData authToken = new AuthData("testUser", "token123");
    authDAO.addAuth(authToken);
    AuthData retrievedAuthToken = authDAO.getAuth("token123");
    Assertions.assertNotNull(retrievedAuthToken);
    Assertions.assertEquals(authToken.getUsername(), retrievedAuthToken.getUsername());
  }

  @Test
  @DisplayName("Retrieve non-existent auth token returns null")
  public void retrieveNonExistentAuthToken() throws DataAccessException {
    AuthData retrievedAuthToken = authDAO.getAuth("nonExistentToken");
    Assertions.assertNull(retrievedAuthToken);
  }

  @Test
  @DisplayName("Delete auth token from database")
  public void deleteAuthTokenFromDatabase() throws DataAccessException {
    AuthData authToken = new AuthData("testUser", "token123");
    authDAO.addAuth(authToken);
    authDAO.deleteAuthorization("token123");
    AuthData retrievedAuthToken = authDAO.getAuth("token123");
    Assertions.assertNull(retrievedAuthToken);
  }

  @Test
  @DisplayName("Check if auth token exists in database")
  public void checkIfAuthTokenExists() throws DataAccessException {
    AuthData authToken = new AuthData("testUser", "token123");
    authDAO.addAuth(authToken);
    Assertions.assertTrue(authDAO.inAuths("token123"));
  }

  @Test
  @DisplayName("Check if non-existent auth token doesn't exist in database")
  public void checkIfNonExistentAuthTokenDoesntExist() throws DataAccessException {
    Assertions.assertFalse(authDAO.inAuths("nonExistentToken"));
  }
}
