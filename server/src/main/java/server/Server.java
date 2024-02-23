package server;

import spark.*;
import dataaccess.*;
import records.*;
import service.*;
import server.handlers.*;

public class Server {

    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    UserService userService;
    GameService gameService;
    ClearService clearService;

    public Server() {
        this.userDAO = new MemoryUserDAO();
        this.gameDAO = new MemoryGameDAO();
        this.authDAO = new MemoryAuthDAO();
        this.userService = new UserService()
    }

    ClearHandler clearHandler = new ClearHandler(userDAO, gameDAO, authDAO);
    RegisterHandler registerHandler = new RegisterHandler(userDAO, gameDAO, authDAO);



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        Spark.delete("/db", (Request req, Response res) -> new ClearHandler.clear(req, res));
//        Spark.post("/user", this::registerUser);
//        Spark.post("/session", (Request req, Response res) -> new LoginHandler.login(req, res));
//        Spark.delete("/session", this::logoutUser);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
