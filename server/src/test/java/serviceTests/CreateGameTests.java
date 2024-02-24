package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import server.requests.CreateGameRequest;
import server.results.CreateGameResult;
import service.ClearService;
import service.GameService;
import service.UserService;

public class CreateGameTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

  @Test
  @DisplayName("Create game works")
  public void createGameWork() throws DataAccessException {
    auths.addAuth(new AuthData("andy", "123"));
    CreateGameRequest createGameRequest = new CreateGameRequest("123", "bruh");
    CreateGameResult result = gameService.createGame(createGameRequest);
    Assertions.assertEquals(result.message(), null);
  }

  @Test
  @DisplayName("Create game ain't work")
  public void createGameNoWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("lol", "nah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("daddy", "12345");
      CreateGameResult result = gameService.createGame(createGameRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }
}
