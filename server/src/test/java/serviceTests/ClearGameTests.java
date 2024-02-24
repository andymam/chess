package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;
import records.*;

public class ClearGameTests {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  UserService userService = new UserService(users, games, auths);
  GameService gameService = new GameService(users, games, auths);
  ClearService clearService = new ClearService(users, games, auths);

  @Test
  @DisplayName("Clear Game work")
  public void ClearGameWork() throws DataAccessException {
    UserData user = new UserData("andy", "pa", "email");
    AuthData auth = new AuthData("andy");
    GameData game = new GameData(1, "gayme");

    users.addUser(user);
    auths.addAuth(auth);
    games.addGame(game);

    Assertions.assertEquals(users.getUsers().size(), 1);
    Assertions.assertEquals(auths.getAuthTokens().size(), 1);
    Assertions.assertEquals(games.getGames().size(), 1);

    clearService.clearAll();

    Assertions.assertEquals(users.getUsers().size(), 0);
    Assertions.assertEquals(auths.getAuthTokens().size(), 0);
    Assertions.assertEquals(games.getGames().size(), 0);

  }
}
