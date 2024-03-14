package server;

import com.google.gson.Gson;
import chess.ChessGame;
import exception.ResponseException;
import org.eclipse.jetty.util.log.Log;
import requests.*;
import results.*;
import spark.Spark;

import java.io.*;
import java.net.*;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
  }

  public RegisterResult register(String username, String password, String email) throws ResponseException {
    var path = "/user";
    var request = new RegisterRequest(username, password, email);
    return makeRequest("POST", path, request, RegisterResult.class);
  }

  public LoginResult login(String username, String password) throws ResponseException {
    var path = "/session";
    var request = new LoginRequest(username, password);
    return makeRequest("POST", path, request, LoginResult.class);
  }

  public LogoutResult logout(String auth) throws ResponseException {
    var path = "/session";
    return makeRequest("DELETE", path, auth, null, null);
  }

  public CreateGameResult createGame(String name, String authToken) throws ResponseException {
    var path = "/game";
    var request = new CreateGameRequest(name);
    return makeRequest("POST", path, authToken, request, CreateGameResult.class);
  }




  private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
    return makeRequest(method, path, null, request, responseClass);
  }

  private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      http.setDoOutput(true);
      if (authToken != null) {
        http.addRequestProperty("authorization", authToken);
      }
      writeBody(request, http);
      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ResponseException(status, "failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }

  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
  public void stop() {
    Spark.stop();
    Spark.awaitStop();
  }
}
