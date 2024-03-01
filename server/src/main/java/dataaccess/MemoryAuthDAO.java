package dataaccess;

import records.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MemoryAuthDAO implements AuthDAO {

  private static final String INSERT = "INSERT into authToken (authToken, username) VALUES (?,?)";


  public MemoryAuthDAO() throws DataAccessException {
    configureDatabase();
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };

  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {

//      one to one
      var createAuthTable = """
                CREATE TABLE IF NOT EXISTS authToken (
                    authToken VARCHAR(255) NOT NULL,
                    username VARCHAR(255) NOT NULL,
                    PRIMARY KEY (authToken),
                    FOREIGN KEY (username) REFERENCES user(username)
                )""";
      try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
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


  ArrayList<AuthData> authTokens = new ArrayList<>();

  public void clearAuths() {
    authTokens.clear();
  }

  public void addAuth(AuthData authToken) {
    authTokens.add(authToken);
  }


  public void deleteAuthorization(String authToken)  {
    authTokens.removeIf(token -> Objects.equals(authToken, token.getAuthToken()));
  }

  public AuthData getAuth(String authToken) {
    for (AuthData auth : authTokens) {
      if (Objects.equals(auth.getAuthToken(), authToken)) {
        return auth;
      }
    }
    return null;
  }

  public boolean inAuths(String authToken) {
    for (AuthData auth : authTokens) {
      if (Objects.equals(auth.getAuthToken(), authToken)) {
        return true;
      }
    }
    return false;
  }

}
