package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.GameData;
import server.requests.CreateGameRequest;
import java.util.Collection;

public class SQLGameDAOTest {

  GameDAO gameDAO;

  @BeforeEach
  void setUp() throws DataAccessException {
    gameDAO = new SQLGameDAO();
    gameDAO.clearGames(); // Clear any existing games before each test
  }

  @Test
  @DisplayName("Add game to database")
  public void addGameToDatabase() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    Assertions.assertNotNull(createdGame);
    Assertions.assertEquals("TestGame", createdGame.getGameName());
  }

  @Test
  @DisplayName("Retrieve game from database")
  public void retrieveGameFromDatabase() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    GameData retrievedGame = gameDAO.getGame(createdGame.getGameID());
    Assertions.assertNotNull(retrievedGame);
    Assertions.assertEquals("TestGame", retrievedGame.getGameName());
  }

  @Test
  @DisplayName("Retrieve all games from database")
  public void retrieveAllGamesFromDatabase() throws DataAccessException {
    gameDAO.addGame(new CreateGameRequest("Game1"));
    gameDAO.addGame(new CreateGameRequest("Game2"));
    Collection<GameData> games = gameDAO.getGames();
    Assertions.assertEquals(2, games.size());
  }

  @Test
  @DisplayName("Set player for a game")
  public void setPlayerForGame() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    Assertions.assertTrue(gameDAO.setPlayer("Player1", "black", createdGame));
    Assertions.assertTrue(gameDAO.setPlayer("Player2", "white", createdGame));
    Assertions.assertFalse(gameDAO.setPlayer("Player3", "black", createdGame)); // Cannot set black player again
  }
}
