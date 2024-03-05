package dataAccess;

import com.google.gson.Gson;
import records.GameData;
import server.requests.CreateGameRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

  DatabaseManager db = new DatabaseManager();

  public SQLGameDAO() throws DataAccessException {
    configureDatabase();
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` INT NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (gameID),
              INDEX(gameName)
            )
            """
  };

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }

  public void clearGames() throws DataAccessException {
    var statement = "TRUNCATE games";
    db.executeUpdate(statement);
  }

  public GameData addGame(CreateGameRequest request) throws DataAccessException {
    var statement = "INSERT INTO games (gameID, gameName, blackUsername, whiteUsername, game, json) VALUES (?, ?, ?, ?, ?, ?)";
    GameData game = new GameData(0, request.getGameName());
    var json = new Gson().toJson(game);
    var id = db.executeUpdate(statement, game.getGameName(), json);
    return new GameData(id, request.getGameName(), game.getGame());
  }

  public GameData getGame(Integer gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT * FROM games WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setInt(1, gameID);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGame(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  public Collection<GameData> getGames() throws DataAccessException {
    Collection<GameData> games = new ArrayList<>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT * FROM games";
      try (var ps = conn.prepareStatement(statement);
           var rs = ps.executeQuery()) {
        while (rs.next()) {
          games.add(readGame(rs));
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return games;
  }


  public boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      String statement;
      if (playerColor.equalsIgnoreCase("black")) {
        statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
      } else if (playerColor.equalsIgnoreCase("white")) {
        statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
      } else {
        // Invalid player color
        return false;
      }
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        ps.setInt(2, game.getGameID());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("Error setting player: %s", e.getMessage()));
    }
  }


  private GameData readGame(ResultSet rs) throws SQLException {
    var game = new GameData(rs.getInt("gameID"), rs.getString("gameName"));
    game.setWhiteUser(rs.getString("whiteUsername"));
    game.setBlackUser(rs.getString("whiteUsername"));
    return game;
  }
}
