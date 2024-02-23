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

import javax.xml.crypto.Data;
import java.util.Collection;

public class GameService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;

  public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO=userDAO;
    this.gameDAO=gameDAO;
    this.authDAO=authDAO;
  }

  public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {

  }

  public Collection<GameData> listGames(ListGamesRequest listGamesRequest) throws DataAccessException {

  }

  public GenericResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {

  }
}
