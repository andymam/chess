package service;

import records.*;
import dataaccess.*;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;
import server.requests.LogoutRequest;
import server.results.LoginResult;
import server.results.LogoutResult;
import server.results.RegisterResult;

import java.util.Objects;

public class UserService {

  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;


  public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
    this.userDAO = userDAO;
    this.authDAO = authDAO;
    this.gameDAO = gameDAO;
  }

  public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
    if (!Objects.equals(registerRequest.getUsername(), null) && !Objects.equals(registerRequest.getPassword(), null) && !Objects.equals(registerRequest.getEmail(), null)) {
      UserData user = userDAO.getUser(registerRequest.getUsername(), registerRequest.getPassword());
      if (user == null) {
        UserData newUser = new UserData(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
        userDAO.addUser(newUser);
        AuthData authToken = new AuthData(registerRequest.getUsername());
        authDAO.addAuth(authToken);
        return new RegisterResult(newUser, authToken);
      }
      throw new DataAccessException("Error: already taken");
    }
    throw new DataAccessException("Error: bad request");
  }


  public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
    UserData user = userDAO.getUser(loginRequest.getUsername(), loginRequest.getPassword());
    if (!Objects.equals(loginRequest.getUsername(), "") && !Objects.equals(loginRequest.getPassword(), "")) {
      if (user != null && Objects.equals(user.getPassword(), loginRequest.getPassword())) {
          AuthData authToken = new AuthData(loginRequest.getUsername());
          authDAO.addAuth(authToken);
          return new LoginResult(user, authToken);
        }
        throw new DataAccessException("Error: unauthorized");
      }
      throw new DataAccessException("Error: bad request");
    }

  public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
    if (!Objects.equals(logoutRequest.getAuth(), "")) {
      if (authDAO.inAuths(logoutRequest.getAuth())) {
        authDAO.deleteAuthorization(logoutRequest.getAuth());
        return new LogoutResult();
      }
      throw new DataAccessException("Error: unauthorized");
    }
    throw new DataAccessException("Error: bad request");
  }
}
