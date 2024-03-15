package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import requests.CreateGameRequest;
import results.CreateGameResult;
import service.GameService;

public class CreateGameTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  GameService gameService = new GameService(users, games, auths);

  @Test
  @DisplayName("Create game works")
  public void createGameWork() throws DataAccessException {
    auths.addAuth(new AuthData("andy", "123"));
    CreateGameRequest createGameRequest = new CreateGameRequest("andy");
    createGameRequest.setAuth("123");
    CreateGameResult result = gameService.createGame(createGameRequest);
    Assertions.assertEquals(result.getMessage(), null);
  }

  @Test
  @DisplayName("Create game ain't work")
  public void createGameNoWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("lol", "nah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("daddy");
      CreateGameResult result = gameService.createGame(createGameRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: bad request");
    }
  }
}
