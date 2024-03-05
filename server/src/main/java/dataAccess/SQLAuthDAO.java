package dataAccess;

import com.google.gson.Gson;
import records.AuthData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

  DatabaseManager db = new DatabaseManager();

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  authTokens (
              `username` varchar(256) NOT NULL,
              `token` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              INDEX(username)
            )
            """
  };

  public SQLAuthDAO() throws DataAccessException {
    configureDatabase();
  }

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database for auths lol: %s", ex.getMessage()));
    }
  }

  public void clearAuths() throws DataAccessException {
    var statement = "TRUNCATE authTokens";
    db.executeUpdate(statement);
  }

  public AuthData getAuth(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT token, json FROM authTokens WHERE token=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readAuth(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  public void addAuth(AuthData authToken) throws DataAccessException {
    var statement = "INSERT INTO authTokens (username, token, json) VALUES (?, ?, ?)";
    var json = new Gson().toJson(authToken);
    db.executeUpdate(statement, authToken.getUsername(), authToken.getAuthToken(), json);
  }
  public void deleteAuthorization(String authToken) throws DataAccessException {
    var statement = "DELETE FROM authTokens WHERE token=?";
    db.executeUpdate(statement, authToken);
  }

  public boolean inAuths(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT COUNT(*) AS count FROM authTokens WHERE token=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            int count = rs.getInt("count");
            return count > 0; // If count is greater than 0, authToken exists
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("Error checking auths: %s", e.getMessage()));
    }
    return false; // Return false if an exception occurs or no result is found
  }


  private AuthData readAuth(ResultSet rs) throws SQLException {
    var json = rs.getString("json");
    return new Gson().fromJson(json, AuthData.class);
  }
}
