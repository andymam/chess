package websocket;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.*;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
public class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;


  public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
          notificationHandler.notify(notification);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void joinPlayer(String auth, int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
    try {
      var joinAction = new JoinPlayerCommand(auth, gameID, playerColor);
      this.session.getBasicRemote().sendText(new Gson().toJson(joinAction));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void joinObserver(String auth, int gameID) throws ResponseException {
    try {
      var joinAction = new JoinObserverCommand(auth, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(joinAction));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void makeMove(String auth, int gameID, ChessMove move) throws ResponseException {
    try {
      var moveAction = new MakeMoveCommand(auth, gameID, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(moveAction));
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void leave(String auth, int gameID) throws ResponseException {
    try {
      var leaveAction = new LeaveCommand(auth, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(leaveAction));
      this.session.close();
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }

  public void resign(String auth, int gameID) throws ResponseException {
    try {
      var resignAction = new ResignCommand(auth, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(resignAction));
      this.session.close();
    } catch (IOException e) {
      throw new ResponseException(500, e.getMessage());
    }
  }
}
