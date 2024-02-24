package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import records.GameData;
import server.requests.ListGamesRequest;
import server.results.ListGamesResult;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler extends Handler {
  public ListGamesHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    super(userDAO, gameDAO, authDAO);
  }

  public Object listGames(Request req, Response res) throws  DataAccessException {
    try {
      ListGamesRequest listGamesRequest = new Gson().fromJson(res.body(), ListGamesRequest.class);
      ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
      res.status(200);
      return new Gson().toJson(listGamesResult);
    }
    catch (DataAccessException exception) {
      res.status(getError(exception.getMessage()));
      return new Gson().toJson(new ListGamesResult(null, exception.getMessage()));
    }
  }
}
