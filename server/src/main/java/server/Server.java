package server;

import com.mysql.cj.log.Log;
import server.requests.CreateGameRequest;
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

//    public Server() {
//        this.userDAO = new MemoryUserDAO();
//        this.gameDAO = new MemoryGameDAO();
//        this.authDAO = new MemoryAuthDAO();
//        this.userService = new UserService()
//    }

    ClearHandler clearHandler = new ClearHandler(userDAO, gameDAO, authDAO);
    RegisterHandler registerHandler = new RegisterHandler(userDAO, gameDAO, authDAO);
    LoginHandler loginHandler = new LoginHandler(userDAO, gameDAO, authDAO);
    LogoutHandler logoutHandler = new LogoutHandler(userDAO, gameDAO, authDAO);
    ListGamesHandler listGamesHandler = new ListGamesHandler(userDAO, gameDAO, authDAO);
    CreateGameHandler createGameHandler = new CreateGameHandler(userDAO, gameDAO, authDAO);
    JoinGameHandler joinGameHandler = new JoinGameHandler(userDAO, gameDAO, authDAO);



    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        Spark.delete("/db", (req, res) -> {
            new ClearHandler(userDAO, gameDAO, authDAO).clear(req, res);
            res.type("application/json");
            return "{}"; // Empty JSON string
        });


//        Spark.delete("/db", (Request req, Response res) -> new ClearHandler(userDAO, gameDAO, authDAO).clear(req, res));
        Spark.post("/user", (Request req, Response res) -> new RegisterHandler(userDAO, gameDAO, authDAO).register(req, res));
        Spark.post("/session", (Request req, Response res) -> new LoginHandler(userDAO, gameDAO, authDAO).login(req, res));
        Spark.delete("/session", (Request req, Response res) -> new LogoutHandler(userDAO, gameDAO, authDAO).logout(req, res));
        Spark.get("/game", (Request req, Response res) -> new ListGamesHandler(userDAO, gameDAO, authDAO).listGames(req, res));
        Spark.post("/game", (Request req, Response res) -> new CreateGameHandler(userDAO, gameDAO, authDAO).createGame(req, res));
        Spark.put("/game", (Request req, Response res) -> new JoinGameHandler(userDAO, gameDAO, authDAO).joinGame(req, res));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
