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
              `username` varchar(256) NOT NULL,
              `token` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              INDEX(username)
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

  public void clearGames() {

  }
  public GameData addGame(CreateGameRequest request) {

  }


  public GameData getGame(Integer gameID) {

  }
  public Collection<GameData> getGames() {

  }

  public boolean setPlayer(String username, String playerColor, GameData game) {

  }

  private GameData readGame(ResultSet rs) throws SQLException {
    var json = rs.getString("json");
    return new Gson().fromJson(json, GameData.class);
  }
}
