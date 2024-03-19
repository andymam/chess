package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import serverFacade.ServerFacade;
import exception.ResponseException;


public class ServerFacadeTests {

    private static Server server;
    public static ServerFacade serverFacade;

    @BeforeEach
    public void setup() {
        Assertions.assertDoesNotThrow(serverFacade::clear);
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        String serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Register work")
    public void register() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
    }

    @Test
    @DisplayName("Register ain't work")
    public void registerFail() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register("username", "password", "email"));
    }

    @Test
    @DisplayName("Login work")
    public void login() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.getAuthToken()));
        Assertions.assertDoesNotThrow(() ->serverFacade.login("username", "password"));
    }

    @Test
    @DisplayName("Login ain't work")
    public void loginFail() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("nah", "lol"));
    }

    @Test
    @DisplayName("Logout work")
    public void logout() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(response.getAuthToken()));
    }

    @Test
    @DisplayName("Logout ain't work")
    public void logoutFail() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout("don't exist"));
    }

    @Test
    @DisplayName("Create game work")
    public void createGame() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game", response.getAuthToken()));
    }

    @Test
    @DisplayName("Create game ain't work")
    public void createGameFail() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame("game", "notAnAuth"));
    }

    @Test
    @DisplayName("List games work")
    public void listGames() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game1", response.getAuthToken()));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game2", response.getAuthToken()));
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames(response.getAuthToken()));
    }

    @Test
    @DisplayName("List games ain't work")
    public void listGamesFail() {
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames("noAuth"));
    }

    @Test
    @DisplayName("Join game work")
    public void joinGame() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        var gameResponse = Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game", response.getAuthToken()));
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameResponse.getGameID(), ChessGame.TeamColor.WHITE, response.getAuthToken()));
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(gameResponse.getGameID(), ChessGame.TeamColor.BLACK, response.getAuthToken()));
    }

    @Test
    @DisplayName("Join game ain't work")
    public void joinGameFail() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(1, ChessGame.TeamColor.WHITE, response.getAuthToken()));
    }

    @Test
    @DisplayName("Join game also ain't work")
    public void otherJoinGameFail() {
        var response = Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        var gameResponse = Assertions.assertDoesNotThrow(() -> serverFacade.createGame("game", response.getAuthToken()));

        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameResponse.getGameID(), ChessGame.TeamColor.BLACK, "fakeAuth"));
    }

    @Test
    @DisplayName("Clear method works")
    public void clearWorks() {
        Assertions.assertDoesNotThrow(() -> serverFacade.clear());
    }

    @Test
    @DisplayName("Clear method fails")
    public void clearFails() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register("username", "password", "email"));
        Assertions.assertDoesNotThrow(() -> serverFacade.clear());
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
