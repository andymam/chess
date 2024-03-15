package dataAccess;

import records.*;
import requests.CreateGameRequest;

import java.util.Collection;

public interface GameDAO {
  void clearGames() throws DataAccessException;
  GameData addGame(CreateGameRequest request) throws DataAccessException;

  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> getGames() throws DataAccessException;

  boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException;

}
