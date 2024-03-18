package results;

import records.GameData;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.util.Collection;

public class ListGamesResult {
  Collection<GameData> games;
  String message;

  public Collection<GameData> getGames() {
    return games;
  }

  public String gamesToString(){
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    for (GameData game: games){
      out.println(game.getGameID()+ " : " + game.getGameName());
    }
    return out.toString();
  }

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
