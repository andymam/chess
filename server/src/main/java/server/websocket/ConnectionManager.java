package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String username, String auth, Session session) {
    var connection = new Connection(username, session);
    connections.put(auth, connection);
  }

  public void remove(String visitorName) {
    connections.remove(visitorName);
  }

  public void broadcast(String authString, NotificationMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.visitorName.equals(authString)) {
          c.send(notification.toString());
        }
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.visitorName);
    }
  }
}