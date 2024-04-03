package server.websocket;

import com.google.gson.Gson;
import dataAccess.*;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
  UserDAO userDAO;
  GameDAO gameDAO;
  AuthDAO authDAO;
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

  }

  private void joinObserver(JoinObserverCommand joinObserverCommand, Session session) throws IOException {

  }

  private void makeMove(MakeMoveCommand makeMoveCommand, Session session) throws IOException {

  }

  private void leave(LeaveCommand leaveCommand, Session session) throws IOException {

  }

  private void resign(ResignCommand resignCommand, Session session) throws IOException {

  }
}
