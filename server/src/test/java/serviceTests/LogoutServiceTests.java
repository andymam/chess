package serviceTests;

import com.mysql.cj.log.Log;
import dataaccess.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import server.requests.LoginRequest;
import server.requests.LogoutRequest;
import server.results.GenericResult;
import server.results.LoginResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import records.*;
import spark.utils.Assert;

public class LogoutServiceTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

  @Test
  @DisplayName("Logout Works")
  public void LogoutWorks() throws  DataAccessException {
    auths.addAuth(new AuthData("andy", "123"));
    LogoutRequest request = new LogoutRequest("123");
    GenericResult result = userService.logout(request);
    Assertions.assertNull(result.message(), "no error");
  }

  @Test
  @DisplayName("Logout ain't work")
  public void LogoutNoWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("daddy", "12345"));
      LogoutRequest request = new LogoutRequest("2");
      GenericResult result = userService.logout(request);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }
}
