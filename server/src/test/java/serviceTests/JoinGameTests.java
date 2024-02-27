package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import server.results.JoinGameResult;
import service.GameService;

public class JoinGameTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  GameService gameService = new GameService(users, games, auths);


  @Test
  @DisplayName("Join game work")
  public void joinGameWork() throws DataAccessException {
    Assertions.assertEquals("true", "true");
  }

  @Test
  @DisplayName("Join game ain't work")
  public void joinGameAintWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("brother", "yah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("fasdf");
      games.addGame(createGameRequest);
      JoinGameRequest joinGameRequest = new JoinGameRequest(4);
      JoinGameResult result = gameService.joinGame(joinGameRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }

}
