package server.results;

import records.GameData;

import java.util.Collection;

public class ListGamesResult {
  Collection<GameData> games;
  String message;

  public ListGamesResult(Collection<GameData> games) {
    this.games = games;
  }

  public ListGamesResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
