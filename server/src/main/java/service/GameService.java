package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import records.AuthData;
import records.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.ListGamesRequest;
import results.CreateGameResult;
import results.JoinGameResult;
import results.ListGamesResult;

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
    if (!Objects.equals(createGameRequest.getGameName(), null) && !Objects.equals(createGameRequest.getAuthorization(), null)) {
      if (authDAO.inAuths(createGameRequest.getAuthorization())) {
        GameData newGame = gameDAO.addGame(createGameRequest);
        return new CreateGameResult(newGame.getGameID());
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }

  public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
    if (!Objects.equals(listGamesRequest.getAuthorization(), "")) {
      if (authDAO.inAuths(listGamesRequest.getAuthorization())) {
        return new ListGamesResult(gameDAO.getGames());
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }

  public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
    if (authDAO.inAuths(joinGameRequest.getAuthorization())) {
      AuthData authToken = authDAO.getAuth(joinGameRequest.getAuthorization());
      GameData game = gameDAO.getGame(joinGameRequest.getGameID());
      if (joinGameRequest.getGameID() != null && game != null) {
        if (gameDAO.setPlayer(authToken.getUsername(), joinGameRequest.getPlayerColor(), game)) {
          return new JoinGameResult();
        }
        throw new DataAccessException("Error: already taken");
      }
      throw new DataAccessException("Error: bad request");
    }
    throw new DataAccessException("Error: unauthorized");
  }
}
