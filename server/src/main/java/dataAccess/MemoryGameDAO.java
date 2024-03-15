package dataAccess;

import records.GameData;
import requests.CreateGameRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {
  ArrayList<GameData> games = new ArrayList<>();
  private int newGameID = 1;

  public void clearGames() {
    games.clear();
  }

  public GameData addGame(CreateGameRequest request) {
    GameData game = new GameData(newGameID++, request.getGameName());
    games.add(game);
    return game;
  }

  public GameData getGame(Integer gameID) {
    for (GameData game : games) {
      if (game.getGameID() == gameID) {
        return game;
      }
    }
    return null;
  }

  public Collection<GameData> getGames() {
    return games;
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
