package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.*;
import server.results.*;
import service.*;
import spark.Request;
import spark.Response;

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

  public String makeJSon(Object object) {
    return new Gson().toJson(object);
  }

  public int getError(String error) {
    if (error.contains("bad request")) {
      return 400;
    } else if (error.contains("unauthorized")) {
      return 401;
    } else if (error.contains("already taken")) {
      return 403;
    } else {
      return 500;
    }
  }
}

class ClearHandler extends Handler {
  public ClearHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }


}

class RegisterHandler extends Handler {
  public RegisterHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object register(Request req, Response res) throws DataAccessException {
    try {
      RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
      RegisterResult registerResult = userService.register(registerRequest);
      res.status(200  );
      return new Gson().toJson(registerResult);
    }
    catch (DataAccessException exception){
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new RegisterResult(null, null, exception.getMessage()));
    }
  }
}

class LoginHandler extends Handler {

}

class LogoutHandler extends Handler {

}

class ListGamesHandler extends Handler {

}

class CreateGameHandler extends Handler {

}

class JoinGameHandler extends Handler {

}
