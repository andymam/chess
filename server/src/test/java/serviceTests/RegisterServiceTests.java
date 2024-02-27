package serviceTests;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.*;
import org.junit.jupiter.api.*;
import records.UserData;
import server.requests.RegisterRequest;
import server.results.RegisterResult;
import service.ClearService;
import service.*;

public class RegisterServiceTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);



  @BeforeEach
  public void fillDB() throws DataAccessException {
    users = new MemoryUserDAO();
    UserData user = new UserData("Andy", "pw", "andy@gmail.com");
    users.addUser(user);
  }

  @AfterEach
  public void reset() throws DataAccessException {
    clearService.clearAll();
  }

  @Test
  @DisplayName("Register service work")
  public void RegisterWorks() throws DataAccessException {
    RegisterRequest registerRequest = new RegisterRequest("John", "password", "deez@gmail.com");
    RegisterResult result = userService.register(registerRequest);
    Assertions.assertEquals(result.getUsername(), "John");
  }

  @Test
  @DisplayName("Register service ain't work")
  public void RegisterFails() throws DataAccessException {
    try {
      RegisterRequest registerRequest =  new RegisterRequest("", "password", "dee");
      userService.register(registerRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: bad request");
    }
  }
}

