package clientTests;

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

    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
