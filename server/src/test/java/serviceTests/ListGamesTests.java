package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.AuthData;
import records.GameData;
import server.requests.CreateGameRequest;
import server.requests.ListGamesRequest;
import server.results.ListGamesResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.utils.Assert;

import java.util.List;

public class ListGamesTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

  @Test
  @DisplayName("List games work")
  public void ListGamesWork() throws DataAccessException {
    auths.addAuth(new AuthData("daddy", "123"));
    CreateGameRequest createGameRequest = new CreateGameRequest("123", "bruh");
    games.addGame(new GameData(3, createGameRequest.gameName()));
    ListGamesRequest listGamesRequest = new ListGamesRequest("123");
    ListGamesResult result = gameService.listGames(listGamesRequest);
    Assertions.assertEquals(result.message(), null);
  }

  @Test
  @DisplayName("List games ain't work")
  public void ListGamesAintWork() throws DataAccessException {
    try {
      auths.addAuth(new AuthData("brother", "yah"));
      CreateGameRequest createGameRequest = new CreateGameRequest("yah", "game");
      games.addGame(new GameData(4, createGameRequest.gameName()));
      ListGamesRequest listGamesRequest = new ListGamesRequest("nah");
      ListGamesResult result = gameService.listGames(listGamesRequest);
    }
    catch (DataAccessException exception) {
      Assertions.assertEquals(exception.getMessage(), "Error: unauthorized");
    }
  }
}
