package dataaccess;

import com.google.gson.Gson;
import records.AuthData;
import records.GameData;
import server.requests.CreateGameRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

  DatabaseManager db = new DatabaseManager();

  public SQLGameDAO() throws DataAccessException {
    configureDatabase();
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` varchar(256) NOT NULL AUTO_INCREMENT,
              `gameName` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `whiteUsername` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              INDEX(gameID)
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

  }


  public GameData getGame(Integer gameID) throws DataAccessException {

  }
  public Collection<GameData> getGames() throws DataAccessException {

  }

  public boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException {

  }

  private GameData readGame(ResultSet rs) throws SQLException {
    var game = new GameData(rs.getInt("gameID"), rs.getString("gameName"));
    game.setWhiteUser(rs.getString("whiteUsername"));
    game.setBlackUser(rs.getString("whiteUsername"));
    return game;
  }
}
