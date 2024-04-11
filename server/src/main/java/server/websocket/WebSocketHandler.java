package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import records.*;
import chess.ChessMove;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
  SQLUserDAO userDAO;
  SQLGameDAO gameDAO;
  SQLAuthDAO authDAO;
  private final ConnectionManager connection = new ConnectionManager();

  public WebSocketHandler() throws DataAccessException {
    try {
      this.userDAO=new SQLUserDAO();
      this.authDAO=new SQLAuthDAO();
      this.gameDAO=new SQLGameDAO();
    } catch (DataAccessException exception) {
      throw new RuntimeException(exception);
    }
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    switch (action.getCommandType()) {
      case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayerCommand.class), session);
      case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserverCommand.class), session);
      case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMoveCommand.class), session);
      case LEAVE -> leave(new Gson().fromJson(message, LeaveCommand.class), session);
      case RESIGN -> resign(new Gson().fromJson(message, ResignCommand.class), session);
    }
  }

  private void joinPlayer(JoinPlayerCommand joinPlayerCommand, Session session) throws IOException {
    try {
      String username = authDAO.getAuth(joinPlayerCommand.getAuthString()).getUsername();
      GameData game = gameDAO.getGame(joinPlayerCommand.getGameID());
      if (joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(game.getBlackUsername(), username)) {
        throw new Exception();
      }
      if (joinPlayerCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(game.getWhiteUsername(), username)) {
        throw new Exception();
      }
      ChessGame chessGame = game.getGame();
      LoadGameMessage loadGameMessage = new LoadGameMessage(chessGame);
      connection.add(joinPlayerCommand.getGameID(), session);
      var message = String.format("%s has joined as %s", username, joinPlayerCommand.getPlayerColor().toString());
      var notification = new NotificationMessage(message);
      connection.broadcast(joinPlayerCommand.getGameID(), session, new Gson().toJson(notification));
      session.getRemote().sendString(new Gson().toJson(loadGameMessage));
    } catch (Exception ex) {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Failed to join the game")));
    }
  }

  private void joinObserver(JoinObserverCommand joinObserverCommand, Session session) throws IOException {
    try {
      String username = authDAO.getAuth(joinObserverCommand.getAuthString()).getUsername();
      GameData game = gameDAO.getGame(joinObserverCommand.getGameID());
      ChessGame chessGame = game.getGame();
      LoadGameMessage loadGameMessage = new LoadGameMessage(chessGame);
      connection.add(joinObserverCommand.getGameID(), session);
      var message = String.format("%s is watching you", username);
      var notification = new NotificationMessage(message);
      connection.broadcast(joinObserverCommand.getGameID(), session, new Gson().toJson(notification));
      session.getRemote().sendString(new Gson().toJson(loadGameMessage));
    } catch (Exception ex) {
      session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Failed to join (observe) the game")));
    }
  }

  private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException {
    try {
      String username = authDAO.getAuth(makeMoveCommand.getAuthString()).getUsername();
      GameData game = gameDAO.getGame(makeMoveCommand.getGameID());
      ChessGame chessGame = game.getGame();
      ChessMove move = makeMoveCommand.getMove();
      if (!Objects.equals(username, game.getWhiteUsername()) && !Objects.equals(username, game.getBlackUsername())) {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage("You cannot make a move as an observer")));
      } else if (chessGame.gameOver()) {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage("The game is over")));
      } else if (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE && !Objects.equals(username, game.getWhiteUsername())) {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage("It is not your (WHITE) turn")));
      } else if (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && !Objects.equals(username, game.getWhiteUsername())) {
        session.getRemote().sendString(new Gson().toJson(new ErrorMessage("It is not your (BLACK) turn")));
      } else {
        chessGame.makeMove(move);
        gameDAO.updateGame(makeMoveCommand.getGameID(), chessGame);
        var message = String.format("%s has moved", username);
        var loadGameMessage = new LoadGameMessage(chessGame);
        var notification = new NotificationMessage(message);
        connection.broadcast(makeMoveCommand.getGameID(), session, new Gson().toJson(notification));
        connection.broadcast(makeMoveCommand.getGameID(), session, new Gson().toJson(loadGameMessage));
      }
    } catch (Exception ex) {
      session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Failed to make a move")));
    }

  }

  private void leave(LeaveCommand leaveCommand, Session session) throws IOException {
    try {
      String username = authDAO.getAuth(leaveCommand.getAuthString()).getUsername();
      if (gameDAO.removePlayer(leaveCommand.getGameID(), username)) {
        var message = String.format("%s has left the game", username);
        var notification = new NotificationMessage(message);
        connection.broadcast(leaveCommand.getGameID(), session, new Gson().toJson(notification));
        connection.remove(leaveCommand.getGameID(), session);
      }
    } catch (Exception ex) {
      session.getRemote().sendString(new Gson().toJson(new ErrorMessage("you ain't leavin")));
    }
  }

  private void resign(ResignCommand resignCommand, Session session) throws IOException {
    try {
      String username = authDAO.getAuth(resignCommand.getAuthString()).getUsername();
      GameData game = gameDAO.getGame(resignCommand.getGameID());
      ChessGame chessGame = game.getGame();
      if (!chessGame.gameOver()) {
        if (Objects.equals(username, game.getWhiteUsername()) || Objects.equals(username, game.getBlackUsername())) {
          if (gameDAO.removePlayer(resignCommand.getGameID(), username)) {
            var message = String.format("%s has quit", username);
            chessGame.finishGame();
            gameDAO.updateGame(resignCommand.getGameID(), chessGame);
            var notification = new NotificationMessage(message);
            connection.broadcast(resignCommand.getGameID(), session, new Gson().toJson(notification));
            connection.remove(resignCommand.getGameID(), session);
          }
        } else {
          session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Game is over")));
        }
      }
    } catch (Exception ex) {
      session.getRemote().sendString(new Gson().toJson(new ErrorMessage("you ain't leavin")));
    }
  }
}
