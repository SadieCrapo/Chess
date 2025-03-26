package client;

import dataaccess.DataAccessException;
import exception.BadRequestException;
import org.junit.jupiter.api.*;
import request.CreateRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.CreateResult;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private RegisterRequest newRegisterRequest = new RegisterRequest("newUser", "newUser", "newUser");
    private RegisterRequest badRegisterRequest = new RegisterRequest("badUser", "badUser", null);
    private LoginRequest newLoginRequest = new LoginRequest("oldUser", "oldUser");
    private CreateRequest newCreateRequest = new CreateRequest("newGame");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setUp() throws BadRequestException, DataAccessException {
        tearDown();
        facade.register(new RegisterRequest("oldUser", "oldUser", "oldUser"));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Successfully register new user")
    public void successRegister() throws BadRequestException {
        RegisterResult result = facade.register(newRegisterRequest);
        Assertions.assertEquals("newUser", result.username());
        Assertions.assertNotEquals("", result.authToken());
    }

    @Test
    @DisplayName("Fail to register new user because no email")
    public void failRegisterNoEmail() {
        String message = "";
        try {
            RegisterResult result = facade.register(badRegisterRequest);
        } catch (BadRequestException e) {
            message = e.getMessage();
        }
        Assertions.assertEquals("Error: bad request", message);
    }

    @Test
    @DisplayName("Fail to register new user because already exists")
    public void failRegisterAlreadyExists() {
        String message = "";
        try {
            RegisterResult result = facade.register(new RegisterRequest("oldUser", "oldUser", "oldUser"));
        } catch (BadRequestException e) {
            message = e.getMessage();
        }
        Assertions.assertEquals("Error: already taken", message);
    }

    @Test
    @DisplayName("Successfully log in user")
    public void successLogin() throws BadRequestException {
        LoginResult result = facade.login(newLoginRequest);
        Assertions.assertEquals("oldUser", result.username());
        Assertions.assertNotEquals("", result.authToken());
    }

    @Test
    @DisplayName("Fail log in because wrong password")
    public void failLoginPassword() {
        String message = "";
        try {
            LoginResult result = facade.login(new LoginRequest("oldUser", "wrong"));
        } catch (BadRequestException e) {
            message = e.getMessage();
        }
        Assertions.assertEquals("Error: unauthorized", message);
    }

    @Test
    @DisplayName("Successfully log out")
    public void successLogout() throws BadRequestException {
        LoginResult login = facade.login(newLoginRequest);
        Assertions.assertDoesNotThrow(() -> facade.logout(login.authToken()));
    }

    @Test
    @DisplayName("Fail to log out because wrong authToken")
    public void failLogoutAuth() throws BadRequestException {
        LoginResult login = facade.login(newLoginRequest);
        Assertions.assertThrows(BadRequestException.class, () -> facade.logout("wrong"));
    }

    @Test
    @DisplayName("Successfully create a game")
    public void successCreate() throws BadRequestException {
        LoginResult login = facade.login(newLoginRequest);
        CreateResult result = facade.create(newCreateRequest, login.authToken());
        Assertions.assertNotNull(result.gameID());
    }

    @Test
    @DisplayName("Fail to create game because wrong authToken")
    public void failCreateAuth() throws BadRequestException {
        LoginResult login = facade.login(newLoginRequest);
        Assertions.assertThrows(BadRequestException.class, () -> facade.create(newCreateRequest,"wrong"));
    }

    public void tearDown() throws DataAccessException {
        server.userDAO.clear();
        server.authDAO.clear();
        server.gameDAO.clear();
    }

}
