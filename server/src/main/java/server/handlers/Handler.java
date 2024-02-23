package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import records.GameData;
import server.requests.*;
import server.results.*;
import service.*;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Objects;

public class Handler {
  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;

  UserService userService;
  GameService gameService;
  ClearService clearService;


  public Handler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO=userDAO;
    this.gameDAO=gameDAO;
    this.authDAO=authDAO;
    this.userService = new UserService(userDAO, gameDAO, authDAO);
    this.gameService = new GameService(userDAO, gameDAO, authDAO);
    this.clearService = new ClearService(userDAO, gameDAO, authDAO);
  }

  public int getError(String error) {
    if (Objects.equals(error, "Error: bad request")) {
      return 400;
    } else if (Objects.equals(error, "unauthorized")) {
      return 401;
    } else if (Objects.equals(error, "Error: already taken")) {
      return 403;
    } else {
      return 500;
    }
  }
}

