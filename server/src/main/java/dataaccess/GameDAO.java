package dataaccess;

import records.*;
import server.requests.CreateGameRequest;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
  void clearGames() throws DataAccessException;
  GameData addGame(CreateGameRequest request) throws DataAccessException;

  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> getGames() throws DataAccessException;

  boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException;

}
