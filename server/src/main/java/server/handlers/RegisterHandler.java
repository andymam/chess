package server.handlers;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import server.requests.RegisterRequest;
import server.results.RegisterResult;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {
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
