package service;

import records.*;
import dataaccess.*;
import server.handlers.*;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;
import server.requests.LogoutRequest;
import server.results.GenericResult;
import server.results.LoginResult;
import server.results.RegisterResult;

import javax.xml.crypto.Data;
import java.util.Objects;
import java.util.function.BinaryOperator;

public class UserService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;


  public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
  }

  public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
    if (!Objects.equals(registerRequest.username(), "") && !Objects.equals(registerRequest.password(), "") && !Objects.equals(registerRequest.email(), "")) {
      UserData user = userDAO.getUser(registerRequest);
      if (user == null) {
        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.addUser(newUser);
        AuthData authToken = new AuthData(registerRequest.username());
        authDAO.addAuth(authToken);
        return new RegisterResult(newUser.getUsername(), authToken.getAuthToken(), null);
      }
      throw new DataAccessException("Error: already taken");
    }
    throw new DataAccessException("Error: bad request");
  }


  public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
    if (!Objects.equals(loginRequest.username(), "") && !Objects.equals(loginRequest.password(), "")) {
      UserData user = userDAO.getUser(loginRequest);
      if (user != null) {
        if (Objects.equals(user.getPassword(), loginRequest.password())) {
          AuthData authToken = new AuthData(loginRequest.username());
          authDAO.addAuth(authToken);
          return new LoginResult(user.getUsername(), authToken.getAuthToken(), null);
        }
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }

  public GenericResult logout(LogoutRequest logoutRequest) throws DataAccessException {
    if (!Objects.equals(logoutRequest.authorization(), "")) {
      if (authDAO.inAuths(logoutRequest.authorization())) {
        authDAO.deleteAuthorization(logoutRequest.authorization());
        return new GenericResult(null);
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }
}
