package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.CreateGameRequest;
import server.results.CreateGameResult;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
  public CreateGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object createGame(Request req, Response res) throws DataAccessException {
    try {
      CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
      CreateGameResult createGameResult = gameService.createGame(createGameRequest);
      res.status(200);
      return new Gson().toJson(createGameResult);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new CreateGameResult(0, exception.getMessage()));
    }
  }
}
