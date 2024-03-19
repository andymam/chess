package ui;

import chess.*;
import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;
import requests.*;
import results.*;
import serverFacade.ServerFacade;

import javax.management.Notification;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ReplUI {
  private String auth = null;
  private final ChessBoardUI boardUI = new ChessBoardUI();
  private final ServerFacade serverFacade = new ServerFacade();
  private State state = State.LOGGEDOUT;


  public String print_menu() {
    String menu = "";
    if (auth == null) {
        menu = """
          - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
          - login <USERNAME> <PASSWORD> - to play chess
          - quit - playing chess
          - help - with possible commands
          """;
    } else {
        menu = """
          - create <NAME> - a game
          - list - games
          - join <ID> [WHITE|BLACK|<empty>] - a game
          - observe <ID> - a game
          - logout - when you are done
          - quit - playing chess
          - help - with possible commands
          """;
    }
    return menu;
  }


  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "login" -> login(params);
        case "register" -> register(params);
        case "logout" -> logout(params);
        case "create" -> createGame(params);
        case "join" -> joinGame(params);
        case "observe" -> watchGame(params);
        case "list" -> listGames();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String login(String... params) throws ResponseException {
    try {
      LoginResult result = serverFacade.login(params[0], params[1]);
      auth = result.getAuthToken();
      return "You are now logged in";
    } catch (Throwable e) {
        return "Failed to log in: " + e.toString();
    }
  }

  public String register(String... params) throws ResponseException {
    try {
      System.out.println(params[0]);
      System.out.println(params[1]);
      System.out.println(params[2]);

      RegisterResult result = serverFacade.register(params[0], params[1], params[2]);
      auth = result.getAuthToken();
      return "You have registered and are now logged in.";
    } catch (Throwable e) {
      return "Failed to register: " + e.toString();
    }
  }

  public String logout(String... params) throws ResponseException {
    try {
      LogoutResult result = serverFacade.logout(auth);
      auth = null;
      return "You have successfully logged out.";
    } catch (Throwable e) {
      return "Failed to log out: " + e.toString();
    }
  }
//
  public String createGame(String... params) throws ResponseException {
    try {
      CreateGameRequest request = new CreateGameRequest(params[0]);
      request.setAuth(auth);
      CreateGameResult result = serverFacade.createGame(request.getGameName(), auth);
      return "Game created";
    } catch (Throwable e) {
      return "Failed to create game: " + e.toString();
    }
  }

  public String joinGame(String... params) throws ResponseException {
    try {
      String gameID = params[0];
      String color = params[1];
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(gameID));
      request.setAuthorization(auth);
//      JoinGameResult result = serverFacade.joinGame(Integer.parseInt(gameID), color, auth);
      boardUI.printBoard();
      return String.format("Joined game %s as %s", gameID, color);
    } catch (Throwable e) {
      return "Failed to join game as a player: " + e.toString();
    }
  }


  public String watchGame(String... params) throws ResponseException {
    try {
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(params[0]));
      request.playerColor = null;
      request.setAuthorization(auth);
//      JoinGameResult result = serverFacade.joinGame(request.getGameID(), null, auth);
      boardUI.printBoard();
      return String.format("Joined game %s as an observer", params[0]);
    } catch (Throwable e) {
      return "Failed to join game as an observer: " + e.toString();
    }
  }

  public String listGames() throws ResponseException {
    try {
      ListGamesResult result = serverFacade.listGames(auth);
      return "Games: " + result.gamesToString();
    } catch (Throwable e) {
      return "Failed to list games: " + e.toString();
    }
  }

  public String help() {
    return "Type in the command you wish to do. It really ain't that hard.";
  }

  private void assertLoggedIn() throws ResponseException {
    if (state == State.LOGGEDOUT) {
      throw new ResponseException(400, "You must log in");
    }
  }

}
