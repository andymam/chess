package websocket;

import webSocketMessages.serverMessages.NotificationMessage;

public class NotificationHandler {
  public void notify(NotificationMessage notification) {
    System.out.println(notification.getNotification());
  }
}