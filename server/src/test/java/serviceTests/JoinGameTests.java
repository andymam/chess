package serviceTests;

import dataaccess.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

  }

  @Test
  @DisplayName("Join game ain't work")
  public void joinGameAintWork() throws DataAccessException {

  }

}
