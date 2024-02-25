package dataaccess;

import records.*;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
  void clearGames() throws DataAccessException;
  GameData addGame(GameData game) throws DataAccessException;

  void deleteGame(GameData game) throws DataAccessException;

  GameData getGame(int gameID) throws DataAccessException;
  Collection<GameData> getGames() throws DataAccessException;
  boolean inGames(int gameID) throws DataAccessException;

  boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException;

//  void updateGame(String gameID, other stuff) throws DataAccessException;
}
