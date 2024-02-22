package dataaccess;

import records.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryGameDAO {
  ArrayList<GameData> games = new ArrayList<>();
  private int newGameID = 0;

  void clearGame() {
    games.clear();
  }
  public GameData addGame(GameData game) {
    for (GameData gayme : games) {
      if (Objects.equals(gayme.getGameID(), game.getGameID())) {
        return null;
      }
    }
    GameData newGame = new GameData(newGameID++, game.getGameName());
    games.add(newGame);
    return newGame;
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

}
