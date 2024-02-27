package dataaccess;

import records.*;
import server.requests.CreateGameRequest;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {
  void clearGames();
  GameData addGame(CreateGameRequest request);

  GameData getGame(Integer gameID);
  Collection<GameData> getGames();

  boolean setPlayer(String username, String playerColor, GameData game);

}
