package client;

import dataaccess.DataAccessException;
import exception.BadRequestException;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import result.RegisterResult;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private RegisterRequest newRegisterRequest = new RegisterRequest("newUser", "newUser", "newUser");
    private RegisterRequest badRegisterRequest = new RegisterRequest("badUser", "badUser", null);

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

    public void tearDown() throws DataAccessException {
        server.userDAO.clear();
        server.authDAO.clear();
        server.gameDAO.clear();
    }

}
