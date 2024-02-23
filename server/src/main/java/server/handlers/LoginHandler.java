package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.LoginRequest;
import server.results.LoginResult;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler {
  public LoginHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object login(Request req, Response res) throws DataAccessException {
    try {
      LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
      LoginResult loginResult = userService.login(loginRequest);
      res.status(200);
      return new Gson().toJson(loginResult);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new LoginResult(null, null, exception.getMessage()));
    }
  }
}
