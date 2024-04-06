package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import records.GameData;
import requests.CreateGameRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.*;
import static java.sql.Types.*;

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
    if (request.getGameName() == null || request.getGameName().isEmpty()) {
      return null; // Reject requests with empty or null game names
    }

    var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
    GameData game = new GameData(1, request.getGameName());
    var json = new Gson().toJson(game.getGame());
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
      if (playerColor == null) {
        return true;
      }
      if (playerColor.equalsIgnoreCase("black") && game.getBlackUsername() == null) {
        statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
      } else if (playerColor.equalsIgnoreCase("white") && game.getWhiteUsername() == null) {
        statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
      } else {
        // Invalid player color or player already assigned
        return false;
      }
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        ps.setInt(2, game.getGameID());
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
          // Update the game object with the assigned player
          if (playerColor.equalsIgnoreCase("black")) {
            game.setBlackUser(username);
          } else {
            game.setWhiteUser(username);
          }
          return true;
        } else {
          return false; // No rows were affected
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("Error setting player: %s", e.getMessage()));
    }
  }

  public boolean updateGame(int gameID, ChessGame game) {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "UPDATE games Set json=? WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, new Gson().toJson(game));
        ps.setInt(2, gameID);
        ps.executeUpdate();
        return true;
      }
    }
    catch (Exception e) {
      return false;
    }
  }

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param instanceof ChessGame p) ps.setString(i + 1, new Gson().toJson(p));
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }



  private GameData readGame(ResultSet rs) throws SQLException {
    var game = new GameData(rs.getInt("gameID"), rs.getString("gameName"));
    game.setWhiteUser(rs.getString("whiteUsername"));
    game.setBlackUser(rs.getString("blackUsername"));
    game.setGame(new Gson().fromJson(rs.getString("json"), ChessGame.class));
    return game;
  }
}
