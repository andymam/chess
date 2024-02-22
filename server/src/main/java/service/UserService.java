package service;

import records.*;
import dataaccess.*;
import handlers.*;
import server.requests.RegisterRequest;
import server.results.RegisterResult;

public class UserService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;


  public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
  }

  public RegisterResult register(RegisterRequest registerRequest) {

  }

  public AuthData login(UserData user) {

  }

  public void logout(UserData user) {

  }

}
