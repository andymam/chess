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
        case "logout" -> logout();
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
      RegisterResult result = serverFacade.register(params[0], params[1], params[2]);
      auth = result.getAuthToken();
      return "You have registered and are now logged in.";
    } catch (Throwable e) {
      return "Failed to register: " + e.toString();
    }
  }

  public String logout() throws ResponseException {
    try {
      serverFacade.logout(auth);
      auth = null;
      return "You have successfully logged out.";
    } catch (Throwable e) {
      return "Failed to log out: " + e.toString();
    }
  }

  public String createGame(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      CreateGameRequest request = new CreateGameRequest(params[0]);
      request.setAuth(auth);
      serverFacade.createGame(request.getGameName(), auth);
      return "Game created";
    } catch (Throwable e) {
      return "Failed to create game: " + e.toString();
    }
  }

  public String joinGame(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      String gameID = params[0];
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(gameID));
      request.setAuthorization(auth);
      String color = params[1];
      ChessGame.TeamColor teamColor;
      if (color == "WHITE") {
        teamColor = ChessGame.TeamColor.WHITE;
      }
      else {
        teamColor = ChessGame.TeamColor.BLACK;
      }

      serverFacade.joinGame(Integer.parseInt(gameID), teamColor, auth);
      boardUI.showWhiteBoard();
      boardUI.showBlackBoard();
      return String.format("Joined game %s as %s", gameID, color);
    } catch (Throwable e) {
      return "Failed to join game as a player: " + e.toString();
    }
  }


  public String watchGame(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(params[0]));
      request.playerColor = null;
      request.setAuthorization(auth);
      serverFacade.joinGame(request.getGameID(), null, auth);
      boardUI.showWhiteBoard();
      boardUI.showBlackBoard();
      return String.format("Joined game %s as an observer", params[0]);
    } catch (Throwable e) {
      return "Failed to join game as an observer: " + e.toString();
    }
  }

  public String listGames() throws ResponseException {
    try {
      assertLoggedIn();
      ListGamesResult result = serverFacade.listGames(auth);
      return "Games: " + result.convertGamesToString();
    } catch (Throwable e) {
      return "Failed to list games: " + e.toString();
    }
  }

  public String help() {
    return "Type in the command you wish to do. It really ain't that hard.";
  }

  private void assertLoggedIn() throws ResponseException {
    if (auth == null) {
      throw new ResponseException(400, "You must log in first.");
    }
  }
}
