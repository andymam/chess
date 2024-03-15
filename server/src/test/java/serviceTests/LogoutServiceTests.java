package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import requests.LogoutRequest;
import results.LogoutResult;
import service.UserService;
import records.*;


public class LogoutServiceTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);

  @Test
  @DisplayName("Logout Works")
  public void LogoutWorks() throws  DataAccessException {
    auths.addAuth(new AuthData("andy", "123"));
    LogoutRequest request = new LogoutRequest("123");
    LogoutResult result = userService.logout(request);
    Assertions.assertNull(result.getMessage(), "no error");
  }

  @Test
  @DisplayName("Logout ain't work")
  public void LogoutNoWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("daddy", "12345"));
      LogoutRequest request = new LogoutRequest("2");
      LogoutResult result = userService.logout(request);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }
}
