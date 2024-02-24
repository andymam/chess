package serviceTests;

import dataaccess.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import server.requests.LoginRequest;
import server.results.LoginResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import records.*;

public class LoginServiceTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

//  @BeforeEach
//  public void fillDB() throws DataAccessException {
//    users = new MemoryUserDAO();
//    UserData user = new UserData("Andy", "pw", "andy@gmail.com");
//    users.addUser(user);
//  }

  @AfterEach
  public void reset() throws DataAccessException {
    clearService.clearAll();
  }

  @Test
  @DisplayName("Login Works")
  public void loginWorks() throws DataAccessException {
    UserData user = new UserData("andy", "pw", "email");
    users.addUser(user);
    LoginRequest loginRequest = new LoginRequest("andy", "pw");
    LoginResult result = userService.login(loginRequest);
    Assertions.assertEquals(result.username(), "andy");
  }

  @Test
  @DisplayName("Login ain't work")
  public void loginNoWork() throws DataAccessException {
    try {
      UserData user = new UserData("andy", "pw", "email");
      users.addUser(user);
      LoginRequest loginRequest = new LoginRequest("bruh", "pass");
      LoginResult  result = userService.login(loginRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }



}
