package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import records.GameData;
import server.requests.CreateGameRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
  @DisplayName("Add game to database - Negative Test (Empty or null game name)")
  public void addGameToDatabaseNegative() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest(""); // Empty game name
    GameData createdGame = gameDAO.addGame(createGameRequest);
    Assertions.assertNull(createdGame); // Assert that no game is created
  }




  @Test
  @DisplayName("Retrieve game from database - Success")
  public void retrieveGameFromDatabaseSuccess() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    GameData retrievedGame = gameDAO.getGame(createdGame.getGameID());
    Assertions.assertNotNull(retrievedGame);
    Assertions.assertEquals("TestGame", retrievedGame.getGameName());
  }

  @Test
  @DisplayName("Retrieve game from database - Failure (Game does not exist)")
  public void retrieveGameFromDatabaseFailure() throws DataAccessException {
    GameData game = gameDAO.getGame(-1); // Providing an invalid game ID
    Assertions.assertNull(game);
  }


  @Test
  @DisplayName("Retrieve all games from database - Success")
  public void retrieveAllGamesFromDatabaseSuccess() throws DataAccessException {
    gameDAO.addGame(new CreateGameRequest("Game1"));
    gameDAO.addGame(new CreateGameRequest("Game2"));
    Collection<GameData> games = gameDAO.getGames();
    Assertions.assertEquals(2, games.size());
  }

  @Test
  @DisplayName("Retrieve all games from database - Failure (No games exist)")
  public void retrieveAllGamesFromDatabaseFailure() throws DataAccessException {
    gameDAO.clearGames(); // Ensure the database is empty
    Collection<GameData> games = gameDAO.getGames();
    Assertions.assertEquals(0, games.size());
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
  @DisplayName("Set player for a game - Positive Test")
  public void setPlayerForGamePositive() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    Assertions.assertTrue(gameDAO.setPlayer("Player1", "black", createdGame));
    Assertions.assertTrue(gameDAO.setPlayer("Player2", "white", createdGame));
  }

  @Test
  @DisplayName("Set player for a game - Negative Test")
  public void setPlayerForGameNegative() throws DataAccessException {
    CreateGameRequest createGameRequest = new CreateGameRequest("TestGame");
    GameData createdGame = gameDAO.addGame(createGameRequest);
    Assertions.assertTrue(gameDAO.setPlayer("Player1", "black", createdGame));
    Assertions.assertFalse(gameDAO.setPlayer("Player1", "black", createdGame)); // Attempting to set black player again
  }

}
