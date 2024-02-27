package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import server.requests.CreateGameRequest;
import server.requests.ListGamesRequest;
import server.results.ListGamesResult;
import service.GameService;

public class ListGamesTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  GameService gameService = new GameService(users, games, auths);


  @Test
  @DisplayName("List games work")
  public void ListGamesWork() throws DataAccessException {
    auths.addAuth(new AuthData("daddy", "123"));
    CreateGameRequest createGameRequest = new CreateGameRequest("daddy");
    games.addGame(createGameRequest);
    ListGamesRequest listGamesRequest = new ListGamesRequest("123");
    ListGamesResult result = gameService.listGames(listGamesRequest);
    Assertions.assertEquals(result.getMessage(), null);
  }

  @Test
  @DisplayName("List games ain't work")
  public void ListGamesAintWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("brother", "yah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("yah");
      games.addGame(createGameRequest);
      ListGamesRequest listGamesRequest = new ListGamesRequest("nah");
      ListGamesResult result = gameService.listGames(listGamesRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }
}
