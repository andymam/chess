package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import records.GameData;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import server.results.GenericResult;
import service.ClearService;
import service.GameService;
import service.UserService;

public class JoinGameTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

  @Test
  @DisplayName("Join game work")
  public void joinGameWork() throws DataAccessException {
    auths.addAuth(new AuthData("daddy", "123"));
    CreateGameRequest createGameRequest = new CreateGameRequest("123", "bruh");
    games.addGame(new GameData(3, createGameRequest.gameName()));
    JoinGameRequest joinGameRequest = new JoinGameRequest("123","WHITE", 3);
    GenericResult result = gameService.joinGame(joinGameRequest);
    Assertions.assertEquals(result.message(), null);
  }

  @Test
  @DisplayName("Join game ain't work")
  public void joinGameAintWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("brother", "yah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("yah", "game");
      games.addGame(new GameData(4, createGameRequest.gameName()));
      JoinGameRequest joinGameRequest = new JoinGameRequest("nah", "WHITE", 4);
      GenericResult result = gameService.joinGame(joinGameRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }

}
