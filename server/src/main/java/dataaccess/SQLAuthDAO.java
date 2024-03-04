package dataaccess;

import com.google.gson.Gson;
import records.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }

  public void clearAuths() throws DataAccessException {
    var statement = "TRUNCATE authTokens";
    db.executeUpdate(statement);
  }

  public AuthData getAuth(String authToken) throws DataAccessException {

  }
  public void addAuth(AuthData authToken) throws DataAccessException {

  }
  public void deleteAuthorization(String authToken) throws DataAccessException {

  }

  public boolean inAuths(String authToken) throws  DataAccessException {

  }

  private AuthData readAuth(ResultSet rs) throws SQLException {
    var json = rs.getString("json");
    return new Gson().fromJson(json, AuthData.class);
  }

}
