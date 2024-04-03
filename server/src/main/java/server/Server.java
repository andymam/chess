package server;

import com.google.gson.Gson;
import requests.*;
import results.*;
import spark.*;
import dataAccess.*;
import service.*;

import java.util.Objects;

public class Server {

    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    UserService userService;
    GameService gameService;
    ClearService clearService;

    public Server(){
        try {
            this.userDAO = new SQLUserDAO();
            this.authDAO = new SQLAuthDAO();
            this.gameDAO = new SQLGameDAO();
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
        this.userService = new UserService(userDAO, gameDAO, authDAO);
        this.gameService = new GameService(userDAO, gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    public static void main(String[] args){
        try{
            int port = Integer.parseInt(args[0]);
            Server temp = new Server();
            temp.run(port);
        }
        catch (Exception exp){
            System.err.println(exp.getMessage());
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");



        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public int getErrorCode(String error) {
        if (Objects.equals(error, "Error: bad request")) {
            return 400;
        } else if (Objects.equals(error, "Error: unauthorized")) {
            return 401;
        } else if (Objects.equals(error, "Error: already taken")) {
            return 403;
        } else {
            return 500;
        }
    }

    private Object clearHandler(Request req, Response res) {
        try {
            Object object = clearService.clearAll();
            res.status(200);
            return new Gson().toJson(object);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new ClearResult(exception.getMessage()));
        }
    }

    private Object registerHandler(Request req, Response res) {
        try {
            RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = userService.register(registerRequest);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new RegisterResult(exception.getMessage()));
        }
    }

    private Object loginHandler(Request req, Response res) {
        try {
            LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = userService.login(loginRequest);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new LoginResult(exception.getMessage()));
        }
    }

    private Object logoutHandler(Request req, Response res) {
        try {
            LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
            LogoutResult logoutResult = userService.logout(logoutRequest);
            res.status(200);
            return new Gson().toJson(logoutResult);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new LogoutResult(exception.getMessage()));
        }
    }

    private Object listGamesHandler(Request req, Response res) {
        try {
            ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
            ListGamesResult result = gameService.listGames(listGamesRequest);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new ListGamesResult(exception.getMessage()));
        }
    }

    private Object createGameHandler(Request req, Response res) {
        try {
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
            createGameRequest.setAuth(req.headers("authorization"));
            CreateGameResult result = gameService.createGame(createGameRequest);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new CreateGameResult(exception.getMessage()));
        }
    }

    private Object joinGameHandler(Request req, Response res) {
        try {
            JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
            joinGameRequest.setAuthorization(req.headers("authorization"));
            JoinGameResult result = gameService.joinGame(joinGameRequest);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException exception) {
            res.status(getErrorCode(exception.getMessage()));
            return new Gson().toJson(new JoinGameResult(exception.getMessage()));
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
