package dataAccess;

import com.google.gson.Gson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import records.UserData;

import java.sql.*;

public class SQLUserDAO implements UserDAO {
  private final DatabaseManager db = new DatabaseManager();

  public SQLUserDAO() throws DataAccessException {
    configureDatabase();
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
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
      throw new DataAccessException(String.format("Unable to configure database for users lol: %s", ex.getMessage()));
    }
  }

  public void addUser(UserData user) throws DataAccessException {
    var statement = "INSERT INTO users (username, password, email, json) VALUES(?, ?, ?, ?)";
    var json = new Gson().toJson(user);
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String hashed = encoder.encode(user.getPassword());
    db.executeUpdate(statement, user.getUsername(), hashed, user.getEmail(), json);
  }

  public void clearUsers() throws DataAccessException {
    var statement = "TRUNCATE users";
    db.executeUpdate(statement);
  }

  public UserData getUser(String username, String password) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, json FROM users WHERE username=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, username);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            String encryptedPass = rs.getString("password");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, encryptedPass)) {
              return readUser(rs);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return null;
  }

  private UserData readUser(ResultSet rs) throws SQLException {
    var json = rs.getString("json");
    return new Gson().fromJson(json, UserData.class);
  }
}
