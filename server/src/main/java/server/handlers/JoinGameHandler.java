package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.JoinGameRequest;
import server.requests.join2request;
import server.results.GenericResult;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
  public JoinGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object joinGame(Request req, Response res) throws DataAccessException {
    try {
      String header = req.headers("authorization");
      join2request joinGameRequest = new Gson().fromJson(req.body(), join2request.class);
      GenericResult result = gameService.joinGame(new JoinGameRequest(header, joinGameRequest.playerColor(), joinGameRequest.gameID()));
      res.status(200);
      return new Gson().toJson(result);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new GenericResult(exception.getMessage()));
    }
  }
}
