package ui;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import requests.*;
import results.*;
import serverFacade.ServerFacade;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import javax.management.Notification;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ReplUI implements NotificationHandler {
  private String auth = null;
  private ChessGame game = null;
  private ChessGame.TeamColor turn = null;
  private Integer gameNum = null;
  private String player = null;


  private WebSocketFacade ws = new WebSocketFacade("http://localhost:8080", this);
//  private WebSocketFacade ws;
  private final ChessBoardUI boardUI = new ChessBoardUI();
  private final Scanner scanner = new Scanner(System.in);
  private final ServerFacade serverFacade = new ServerFacade();

  public ReplUI() throws ResponseException {}

  public String print_menu() {
    String menu = "";
    if (auth == null) {
        menu = """
          - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
          - login <USERNAME> <PASSWORD> - to play chess
          - quit - playing chess
          - help - with possible commands
          """;
    } else if (game != null && Objects.equals(player, "player")) {
        menu = """
          - move - make a move
          - redraw - show the chess board again
          - highlight - mark possible moves
          - leave - dip out of the game
          - resign - give up
          - help - with possible commands
          """;
    } else if (game != null && Objects.equals(player, "observer")) {
      menu = """
          - redraw - show the chess board again
          - leave - dip out of the game
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
        case "move" -> makeMove(params);
        case "redraw" -> redraw(params);
        case "highlight" -> highlight(params);
        case "leave" -> leave(params);
        case "resign" -> resign(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (ResponseException ex) {
      return ex.getMessage();
    }
  }

  public String makeMove(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      System.out.println("Enter the start position ('row col'): ");
      String startInput = scanner.next();
      var tokens = startInput.split("");
      ChessPosition start = new ChessPosition(Integer.parseInt(tokens[0]), tokens[1].charAt(0) - 'a');
      ChessPiece piece = game.getBoard().getPiece(start);
      if (piece == null || piece.getTeamColor() != turn) {
        return "Not a valid start position";
      }
      System.out.println("Enter the end position ('row col'): ");
      String endInput = scanner.next();
      var endTokens = endInput.split("");
      ChessPiece.PieceType promotion = null;
      if (piece.getPieceType() == ChessPiece.PieceType.PAWN && (turn == ChessGame.TeamColor.WHITE && Integer.parseInt(endTokens[0]) == 8) || (turn == ChessGame.TeamColor.BLACK && Integer.parseInt(endTokens[0]) == 1)) {
        System.out.println("Which piece would you like to promote to?\nQ - Queen\nK - Knight\nB - Bishop\nR - Rook");
        String promotionPiece = scanner.next();
        promotion = selectPromotion(promotionPiece);
      }
      ChessPosition endPosition = new ChessPosition(Integer.parseInt(endTokens[0]), endTokens[1].charAt(0) - 'a');
      ChessMove move = new ChessMove(start, endPosition, promotion);
      game.makeMove(move);
      ws.makeMove(auth, gameNum, move);
      return String.format("Moved from %s to %s", start, endPosition);
    }
    catch (InvalidMoveException e) {
      return "Not a valid move";
    } catch (ResponseException e) {
      return "Could not make a move";
    } catch (NumberFormatException e) {
      return "Position invalid";
    }
  }

  private ChessPiece.PieceType selectPromotion(String promotion) {
    return switch (promotion) {
      case "Q" -> ChessPiece.PieceType.QUEEN;
      case "K" -> ChessPiece.PieceType.KNIGHT;
      case "B" -> ChessPiece.PieceType.BISHOP;
      case "R" -> ChessPiece.PieceType.ROOK;
      default -> null;
    };
  }

  public String redraw(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      boardUI.showBoard(game, turn, false, null);
      return "Redrawing board";
    } catch (Throwable e) {
      return "Failed to redraw the board: " + e;
    }
  }

  public String highlight(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      return "Invalid highlight command";
    } catch (Throwable e) {
      return "Failed to highlight moves: " + e;
    }
  }

  public String leave(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      ws.leave(auth, gameNum);
      game = null;
      player = null;
      turn = null;
      gameNum = null;
      return "You have left the game";
    } catch (Throwable e) {
      return "Failed to leave game: " + e;
    }
  }

  public String resign(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      ws.resign(auth, gameNum);
      game = null;
      player = null;
      turn = null;
      gameNum = null;
      return "You have resigned";
    } catch (Throwable e) {
      return "Failed to resign: " + e;
    }
  }

  public String login(String... params) throws ResponseException {
    try {
      LoginResult result = serverFacade.login(params[0], params[1]);
      auth = result.getAuthToken();
      return "You are now logged in";
    } catch (Throwable e) {
        return "Failed to log in: " + e;
    }
  }

  public String register(String... params) throws ResponseException {
    try {
      RegisterResult result = serverFacade.register(params[0], params[1], params[2]);
      auth = result.getAuthToken();
      return "You have registered and are now logged in.";
    } catch (Throwable e) {
      return "Failed to register: " + e;
    }
  }

  public String logout() throws ResponseException {
    try {
      serverFacade.logout(auth);
      auth = null;
      return "You have successfully logged out.";
    } catch (Throwable e) {
      return "Failed to log out: " + e;
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
      return "Failed to create game: " + e;
    }
  }

  public String joinGame(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      String gameID = params[0];
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(gameID));
      request.setAuthorization(auth);
      request.playerColor = params[1];
      turn = (Objects.equals(request.playerColor, "WHITE")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
      ChessGame.TeamColor teamColor;
      if (request.playerColor == "WHITE") {
        teamColor = ChessGame.TeamColor.WHITE;
      }
      else {
        teamColor = ChessGame.TeamColor.BLACK;
      }
      serverFacade.joinGame(Integer.parseInt(gameID), teamColor, auth);
      ws.joinPlayer(auth, Integer.parseInt(gameID), turn);
      gameNum = Integer.parseInt(gameID);
      player = "player";
      return String.format("Joined game %s as %s", gameID, request.playerColor);
    } catch (Throwable e) {
      return "Failed to join game as a player: " + e;
    }
  }


  public String watchGame(String... params) throws ResponseException {
    try {
      assertLoggedIn();
      String gameID = params[0];
      JoinGameRequest request = new JoinGameRequest(Integer.parseInt(params[0]));
      request.playerColor = null;
      request.setAuthorization(auth);
      serverFacade.joinGame(request.getGameID(), null, auth);
      ws.joinObserver(auth, Integer.parseInt(gameID));
      player = "observer";
      gameNum = Integer.parseInt(gameID);
      return String.format("Joined game %s as an observer", params[0]);
    } catch (Throwable e) {
      return "Failed to join game as an observer: " + e;
    }
  }

  public String listGames() throws ResponseException {
    try {
      assertLoggedIn();
      ListGamesResult result = serverFacade.listGames(auth);
      return "Games: " + result.convertGamesToString();
    } catch (Throwable e) {
      return "Failed to list games: " + e;
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

  public void notify(String message) {
    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
    switch (serverMessage.getServerMessageType()) {
      case NOTIFICATION -> notificate((new Gson().fromJson(message, NotificationMessage.class)));
      case LOAD_GAME -> loadGame((new Gson().fromJson(message, LoadGameMessage.class)));
      case ERROR -> doError((new Gson().fromJson(message, ErrorMessage.class)));
    }
}

    private void notificate(NotificationMessage notificationMessage) {
    System.out.print(SET_TEXT_COLOR_RED + notificationMessage.getNotification());
    }

    private void loadGame(LoadGameMessage loadGameMessage) {
      game = loadGameMessage.getChessGame();
      boardUI.showBoard(game, turn, false, null);
    }

    private void doError(ErrorMessage errorMessage) {
    System.out.print(SET_TEXT_COLOR_RED + errorMessage.getErrorMessage());
    }
}
