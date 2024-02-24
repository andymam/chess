package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import records.GameData;
import server.requests.CreateGameRequest;
import server.requests.*;
import server.requests.JoinGameRequest;
import server.results.CreateGameResult;
import server.results.GenericResult;
import server.results.ListGamesResult;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Objects;

public class GameService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;

  private int newGameID = 1;

  public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO=userDAO;
    this.gameDAO=gameDAO;
    this.authDAO=authDAO;
  }

  public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
    if (!Objects.equals(createGameRequest.gameName(), "") && !Objects.equals(createGameRequest.authorization(), "")) {
      if (authDAO.inAuths(createGameRequest.authorization())) {
        GameData newGame = new GameData(newGameID++, createGameRequest.gameName());
        gameDAO.addGame(newGame);
        return new CreateGameResult(newGame.getGameID(), null);
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }

  public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
    if (!Objects.equals(listGamesRequest.authorization(), "")) {
      if (authDAO.inAuths(listGamesRequest.authorization())) {
        return new ListGamesResult(gameDAO.getGames(), null);
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }

//  public GenericResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
//
//  }
}
