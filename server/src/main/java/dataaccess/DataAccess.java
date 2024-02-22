package dataaccess;


import records.*;
import java.util.Collection;

public interface DataAccess {

  void clear() throws DataAccessException;
  UserData addUser(UserData user) throws DataAccessException;
  UserData getUser(String username) throws DataAccessException;
  GameData addGame(GameData game) throws DataAccessException;

  void deleteGame(GameData game) throws DataAccessException;

GameData getGame(int gameID) throws DataAccessException;
Collection<GameData> getGames() throws DataAccessException;
Collection<UserData> getUsers() throws DataAccessException;
  Collection<AuthData> getAuthTokens() throws DataAccessException;
  void addAuth(AuthData authToken) throws DataAccessException;
  boolean getAuthorization(String authToken) throws DataAccessException;
  void deleteAuthorization(String authToken) throws DataAccessException;

//  updateGame



}
