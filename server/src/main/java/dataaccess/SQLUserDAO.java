package dataaccess;

import records.UserData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLUserDAO implements UserDAO {
  private final DatabaseManager db = new DatabaseManager();

  public SQLUserDAO() throws DataAccessException {
    configureDatabase();
  }

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      var createUserTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";
      try (var createTableStatement = conn.prepareStatement(createUserTable)) {
        createTableStatement.executeUpdate();
      }
      catch (SQLException exception) {
        throw new DataAccessException(exception.getMessage());
      }
    }
    catch (SQLException exception) {
      throw new DataAccessException(exception.toString());
    }
  }

  public void addUser(UserData user) throws DataAccessException {
    try (var conn = db.getConnection()) {
      var insertUserQuery = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
      try (var insertStatement = conn.prepareStatement(insertUserQuery)) {
        insertStatement.setString(1, user.getUsername());
        insertStatement.setString(2, user.getPassword());
        insertStatement.setString(3, user.getEmail());
        insertStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new DataAccessException(exception.getMessage());
      }
    } catch (SQLException exception) {
      throw new DataAccessException(exception.toString());
    }
  }

  public void clearUsers() throws DataAccessException {
    try (var conn = db.getConnection()) {
      var clearUsersQuery = "DELETE FROM user";
      try (var clearStatement = conn.prepareStatement(clearUsersQuery)) {
        clearStatement.executeUpdate();
      } catch (SQLException exception) {
        throw new DataAccessException(exception.getMessage());
      }
    } catch (SQLException exception) {
      throw new DataAccessException(exception.toString());
    }
  }

  public UserData getUser(String username) throws DataAccessException {
    try (var conn = db.getConnection()) {
      var selectUserQuery = "SELECT * FROM user WHERE username = ?";
      try (var selectStatement = conn.prepareStatement(selectUserQuery)) {
        selectStatement.setString(1, username);
        try (var resultSet = selectStatement.executeQuery()) {
          if (resultSet.next()) {
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            return new UserData(username, password, email);
          } else {
            return null; // User not found
          }
        }
      } catch (SQLException exception) {
        throw new DataAccessException(exception.getMessage());
      }
    } catch (SQLException exception) {
      throw new DataAccessException(exception.toString());
    }
  }

  public List<UserData> getAllUsers() throws DataAccessException {
    List<UserData> users = new ArrayList<>();
    try (var conn = db.getConnection()) {
      var selectAllUsersQuery = "SELECT * FROM user";
      try (var selectStatement = conn.prepareStatement(selectAllUsersQuery)) {
        try (var resultSet = selectStatement.executeQuery()) {
          while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String email = resultSet.getString("email");
            users.add(new UserData(username, password, email));
          }
        }
      } catch (SQLException exception) {
        throw new DataAccessException(exception.getMessage());
      }
    } catch (SQLException exception) {
      throw new DataAccessException(exception.toString());
    }
    return users;
  }

}
