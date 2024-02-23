package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.JoinGameRequest;
import server.results.GenericResult;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
  public JoinGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object joinGame(Request req, Response res) throws DataAccessException {
    try {
      JoinGameRequest joinGameRequest = new Gson().fromJson(res.body(), JoinGameRequest.class);
      GenericResult result = gameService.joinGame(joinGameRequest);
      res.status(200);
      return new Gson().toJson(result);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new GenericResult(exception.getMessage()));
    }
  }
}
