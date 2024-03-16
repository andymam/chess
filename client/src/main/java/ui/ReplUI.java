package ui;

import chess.*;
import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;
import serverFacade.ServerFacade;

import javax.management.Notification;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ReplUI {

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
        case "observe game" -> observeGame(params);
        case "list games" -> listGames(params);
        case "clear" -> clear();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String login(String... Params) throws ResponseException {

  }

  public String register(String... Params) throws ResponseException {

  }

  public String logout(String... Params) throws ResponseException {

  }

  public String createGame(String... Params) throws ResponseException {

  }

  public String joinGame(String... Params) throws ResponseException {

  }

  public String observeGame(String... Params) throws ResponseException {

  }

  public String listGames(String... Params) throws ResponseException {

  }
  public String clear() throws ResponseException {

  }

  public String help() {

  }

}
