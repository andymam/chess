package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class GameService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;

  public GameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO=userDAO;
    this.gameDAO=gameDAO;
    this.authDAO=authDAO;
  }
}
