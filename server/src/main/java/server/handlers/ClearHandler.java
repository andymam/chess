package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.results.GenericResult;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
  public ClearHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object clear(Request req, Response res) throws DataAccessException {
    try {
      clearService.clearAll();
      res.status(200);
      return null;
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new GenericResult(exception.getMessage()));
    }
  }
}
