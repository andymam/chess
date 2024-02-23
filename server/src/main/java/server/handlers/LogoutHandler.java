package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.LogoutRequest;
import server.results.GenericResult;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
  public LogoutHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object logout(Request req, Response res) throws DataAccessException {
    try {
      LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
      GenericResult result = userService.logout(logoutRequest);
      res.status(200);
      return new Gson().toJson(logoutRequest);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new GenericResult(exception.getMessage()));
    }
  }
}
