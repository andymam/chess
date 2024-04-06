package dataAccess;

import chess.ChessGame;
import records.*;
import requests.CreateGameRequest;

import java.util.Collection;

public interface GameDAO {
  void clearGames() throws DataAccessException;
  GameData addGame(CreateGameRequest request) throws DataAccessException;

  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> getGames() throws DataAccessException;

  public boolean updateGame(int gameID, ChessGame game);

  boolean setPlayer(String username, String playerColor, GameData game) throws DataAccessException;

}
