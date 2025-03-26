package client;

import dataaccess.DataAccessException;
import exception.BadRequestException;
import model.GameData;
import org.junit.jupiter.api.*;
import request.*;
import result.*;
import server.Server;

import java.util.ArrayList;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private RegisterRequest newRegisterRequest = new RegisterRequest("newUser", "newUser", "newUser");
    private RegisterRequest badRegisterRequest = new RegisterRequest("badUser", "badUser", null);
    private LoginRequest newLoginRequest = new LoginRequest("oldUser", "oldUser");
    private LoginRequest oldLoginRequest = new LoginRequest("superOldUser", "superOldUser");
    private CreateRequest oldCreateRequest = new CreateRequest("oldGame");
    private CreateRequest newCreateRequest = new CreateRequest("newGame");
    private LoginResult loginResult;
    private CreateResult createResult;

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
        facade.register(new RegisterRequest("superOldUser", "superOldUser", "superOldUser"));
        loginResult = facade.login(oldLoginRequest);
        createResult = facade.create(oldCreateRequest, loginResult.authToken());
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
    public void successLogout() {
        Assertions.assertDoesNotThrow(() -> facade.logout(loginResult.authToken()));
    }

    @Test
    @DisplayName("Fail to log out because wrong authToken")
    public void failLogoutAuth() {
        Assertions.assertThrows(BadRequestException.class, () -> facade.logout("wrong"));
    }

    @Test
    @DisplayName("Successfully create a game")
    public void successCreate() throws BadRequestException {
        Assertions.assertEquals(1, createResult.gameID());
        CreateResult result = facade.create(newCreateRequest, loginResult.authToken());
        Assertions.assertEquals(2, result.gameID());
    }

    @Test
    @DisplayName("Fail to create game because wrong authToken")
    public void failCreateAuth() throws BadRequestException {
        Assertions.assertThrows(BadRequestException.class, () -> facade.create(newCreateRequest,"wrong"));
    }

    @Test
    @DisplayName("Fail to create game because no name")
    public void failCreateNoName() throws BadRequestException {
        Assertions.assertThrows(BadRequestException.class, () -> facade.create(new CreateRequest(null), loginResult.authToken()));
    }

    @Test
    @DisplayName("Successfully list games")
    public void successList() throws BadRequestException, DataAccessException {
        CreateResult create = facade.create(new CreateRequest("newGame"), loginResult.authToken());
        ListResult result = facade.list(loginResult.authToken());


        ArrayList<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(server.gameDAO.getGame(createResult.gameID()));
        expectedGames.add(server.gameDAO.getGame(create.gameID()));

        Assertions.assertEquals(expectedGames, result.games());
    }

    @Test
    @DisplayName("Fail to create game because wrong authToken")
    public void failListAuth() {
        Assertions.assertThrows(BadRequestException.class, () -> facade.list("wrong"));
    }

    @Test
    @DisplayName("Successfully join game")
    public void successJoin() {
        Assertions.assertDoesNotThrow(() -> facade.join(new JoinRequest("WHITE", createResult.gameID()), loginResult.authToken()));
    }

    @Test
    @DisplayName("Fail to join game because wrong color")
    public void failJoinColor() {
        Assertions.assertThrows(BadRequestException.class, () -> facade.join(new JoinRequest("PINK", 1), loginResult.authToken()));
    }

    public void tearDown() throws DataAccessException {
        server.userDAO.clear();
        server.authDAO.clear();
        server.gameDAO.clear();
    }

}
