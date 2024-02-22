package dataaccess;

import records.*;
import java.util.Collection;

public interface GameDAO {
  void clearGames() throws DataAccessException;
  GameData addGame(GameData game) throws DataAccessException;

  void deleteGame(GameData game) throws DataAccessException;

  GameData getGame(int gameID) throws DataAccessException;
  Collection<GameData> getGames() throws DataAccessException;
}
