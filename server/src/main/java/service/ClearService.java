package service;

import dataaccess.*;
import records.*;
import server.*;
import server.results.GenericResult;

import javax.xml.crypto.Data;

public class ClearService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;

  public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.gameDAO = gameDAO;
    this.authDAO = authDAO;
  }

  void clearUsers() throws DataAccessException {
    userDAO.clearUsers();
  }

  void clearGames() throws DataAccessException {
    gameDAO.clearGames();
  }

  void clearAuths() throws DataAccessException {
    authDAO.clearAuths();
  }

  public void clearAll() throws DataAccessException {
    clearGames();
    clearUsers();
    clearAuths();
  }
}
