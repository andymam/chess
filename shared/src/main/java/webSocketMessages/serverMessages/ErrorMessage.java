package webSocketMessages.serverMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class ErrorMessage extends ServerMessage {
  String errorMessage;
  public ErrorMessage(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
