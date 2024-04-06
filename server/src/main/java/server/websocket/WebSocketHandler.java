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
  public void onMessage(Session session, String message) throws InvalidMoveException, DataAccessException, IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    switch (action.getCommandType()) {
      case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayerCommand.class), session);
      case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserverCommand.class), session);
      case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMoveCommand.class), session);
      case LEAVE -> leave(new Gson().fromJson(message, LeaveCommand.class), session);
      case RESIGN -> resign(new Gson().fromJson(message, ResignCommand.class), session);
    }
  }

  private LoadGameMessage joinPlayer(JoinPlayerCommand joinPlayerCommand, Session session) throws DataAccessException, IOException {
    String username = authDAO.getAuth(joinPlayerCommand.getAuthString()).getUsername();
    GameData game = gameDAO.getGame(joinPlayerCommand.getGameID());
    ChessGame chessGame = game.getGame();
    LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
    connection.add(username, joinPlayerCommand.getAuthString(), session);
    var message = String.format("%s has joined as %s", username, joinPlayerCommand.getPlayerColor());
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connection.broadcast(joinPlayerCommand.getAuthString(), notification);
    return loadGameMessage;
  }

  private LoadGameMessage joinObserver(JoinObserverCommand joinObserverCommand, Session session) throws DataAccessException, IOException {
    String username = authDAO.getAuth(joinObserverCommand.getAuthString()).getUsername();
    connection.add(username, joinObserverCommand.getAuthString(), session);
    GameData game = gameDAO.getGame(joinObserverCommand.getGameID());
    ChessGame chessGame = game.getGame();
    LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
    var message = String.format("%s is watching you", username);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connection.broadcast(joinObserverCommand.getAuthString(), notification);
    return loadGameMessage;
  }

  private LoadGameMessage makeMove(MakeMoveCommand makeMoveCommand, Session session) throws InvalidMoveException, DataAccessException, IOException {
    String username = authDAO.getAuth(makeMoveCommand.getAuthString()).getUsername();
    GameData game = gameDAO.getGame(makeMoveCommand.getGameID());
    ChessGame chessGame = game.getGame();
    ChessMove move = makeMoveCommand.getMove();
    chessGame.makeMove(move);
    gameDAO.updateGame(makeMoveCommand.getGameID(), chessGame);
    var message = String.format("%s has moved", username);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connection.broadcast(makeMoveCommand.getAuthString(), notification);;
    return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
  }

  private void leave(LeaveCommand leaveCommand, Session session) throws DataAccessException, IOException {
    String username = authDAO.getAuth(leaveCommand.getAuthString()).getUsername();
    connection.remove(leaveCommand.getAuthString());
    var message = String.format("%s has left the game", username);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connection.broadcast(leaveCommand.getAuthString(), notification);
  }

  private void resign(ResignCommand resignCommand, Session session) throws DataAccessException, IOException {
    String username = authDAO.getAuth(resignCommand.getAuthString()).getUsername();
    connection.remove(resignCommand.getAuthString());
    var message = String.format("%s has resigned", username);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connection.broadcast(resignCommand.getAuthString(), notification);
  }
}
