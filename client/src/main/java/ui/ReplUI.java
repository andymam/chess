package ui;

import chess.*;
import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;
import serverFacade.ServerFacade;

import javax.management.Notification;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ReplUI {
  private String visitorName = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.LOGGEDOUT;

  public ReplUI(String serverUrl) {
    server = new ServerFacade(serverUrl);
    this.serverUrl = serverUrl;
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
        case "create game" -> createGame(params);
        case "join game" -> joinGame(params);
        case "watch game" -> watchGame(params);
        case "list games" -> listGames(params);
        case "clear" -> clear();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String login(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
        return "failed to log in: " + e.toString();
    }
  }

  public String register(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String logout(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String createGame(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String joinGame(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String watchGame(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String listGames(String... params) throws ResponseException {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }
  public String clear() throws ResponseException {
    try {

    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

  public String help() {
    try {
      return params[5];
    } catch (Throwable e) {
      return "failed to log in: " + e.toString();
    }
  }

}
