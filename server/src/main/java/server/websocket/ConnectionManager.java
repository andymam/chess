package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

  public void add(int gameID, Session session) {
    ArrayList<Session> sessions = connections.get(gameID);
    if (sessions == null) {
      connections.put(gameID, new ArrayList<>());
      sessions = connections.get(gameID);
    }
    sessions.add(session);
  }

  public void remove(int gameID, Session session) {
    var connections2 = connections.get(gameID);
    connections2.remove(session);
  }

  public void broadcast(int gameID, Session session, String notification) throws IOException {
    var removeList = new ArrayList<>();
    var connections2 = connections.get(gameID);
    for (var c : connections2) {
      if (c.isOpen()) {
        if (!c.equals(session)) {
          session.getRemote().sendString(notification);
        }
      } else {
        removeList.add(c);
      }
    }
    for (var connection : removeList) {
      connections2.remove(connection);
    }
  }
}