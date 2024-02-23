package server;

import spark.*;
import dataaccess.*;
import records.*;
import service.*;

public class Server {

//    DataAccess data = new MemoryDataAccess();
    UserDAO userDAO = new MemoryUserDAO();
    GameDAO gameDAO = new MemoryGameDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

//    ClearHandler clearHandler = new ClearHandler(userDAO, gameDAO, authDAO);
//    RegisterHandler registerHandler = new RegisterHandler(data);

//    public static void main(String[] args) {
//        try {
//            int port = Integer.parseInt(args[0]);
//            Server temp = new Server();
//            temp.run(port);
//        } catch (Exception exp) {
//            System.err.println(exp.getMessage());
//        }
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
//        Spark.delete("/db", (Request req, Response res) -> new ClearHandler.clear(req, res));
//        Spark.post("/user", this::registerUser);
//        Spark.post("/session", (Request req, Response res) -> new LoginHandler.login(req, res));
//        Spark.delete("/session", this::logoutUser);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
//        Spark.get("/hello", (req, res) -> "Hello BYU!");
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
