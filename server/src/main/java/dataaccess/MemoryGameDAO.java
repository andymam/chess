package dataaccess;

import records.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
  ArrayList<GameData> games = new ArrayList<>();
//  private int newGameID = 1;

  public void clearGames() {
    games.clear();
  }
  public GameData addGame(GameData game) {
    for (GameData gayme : games) {
      if (Objects.equals(gayme.getGameID(), game.getGameID())) {
        return null;
      }
    }
    games.add(game);
    return game;
  }

  public GameData getGame(int gameID) {
    for (GameData game : games) {
      if (Objects.equals(game.getGameID(), gameID)) {
        return game;
      }
    }
    return null;
  }

  public Collection<GameData> getGames() {
    return games;
  }

  public void deleteGame(GameData game) {
    games.remove(game);
  }

  public boolean inGames(int gameID) {
    for (GameData game : games) {
      if (Objects.equals(game.getGameID(), gameID)) {
        return true;
      }
    }
    return false;
  }

  public boolean setPlayer(String username, String playerColor, GameData game) {
    if (Objects.equals(playerColor, null)) {
      return true;
    } else if (Objects.equals(playerColor, "WHITE") && Objects.equals(game.getWhiteUsername(), null)) {
      game.setWhiteUser(username);
      return true;
    } else if (Objects.equals(playerColor, "BLACK") && Objects.equals(game.getBlackUsername(), null)) {
      game.setBlackUser(username);
      return true;
    }
    return false;
  }

}
