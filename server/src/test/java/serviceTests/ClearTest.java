package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateGameRequest;
import service.ClearService;
import records.*;

public class ClearTest {

  UserDAO users = new MemoryUserDAO();
  GameDAO games = new MemoryGameDAO();
  AuthDAO auths = new MemoryAuthDAO();
  ClearService clearService = new ClearService(users, games, auths);

  @AfterEach
  public void reset() throws DataAccessException {
    clearService.clearAll();
  }

  @Test
  @DisplayName("Clear work")
  public void ClearWork() throws DataAccessException {
    CreateGameRequest request = new CreateGameRequest("game");
    games.addGame(request);
    UserData user = new UserData("andy", "no", "nah");
    users.addUser(user);
    AuthData auth = new AuthData("andy");
    auths.addAuth(auth);
    clearService.clearAll();
    Assertions.assertEquals(games.getGames().size(), 0);

  }
}
