package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.TestAuthResult;
import request.LoginRequest;
import result.LoginResult;
import server.Server;
import service.UnauthorizedException;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class LoginServiceTests {
    UserData existingUser = new UserData("username", "password", "email");
    UserData sameUser = new UserData("username", "password", "email");
    UserData differentUser = new UserData("diffname", "diffpassword", "diffemail");

    @BeforeAll
    public static void setup() {
        Server testServer = new Server();
        Server.userDAO = new MemoryUserDAO();
        Server.gameDAO = new MemoryGameDAO();
        Server.authDAO = new MemoryAuthDAO();

    }

    @Test
    @DisplayName("Successful Login")
    public void successLogin() throws UnauthorizedException {
        Server.userDAO.createUser(existingUser);
        LoginResult result = UserService.login(new LoginRequest("username", "password"));

        Assertions.assertEquals(existingUser.username(), result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Failed Login because no Username in db")
    public void failLoginUsername() throws UnauthorizedException {
        Assertions.assertThrows(UnauthorizedException.class, () -> {UserService.login(new LoginRequest("diffname", "diffpassword"));});
    }

    @Test
    @DisplayName("Failed Login because Wrong Password")
    public void failLoginPassword() throws UnauthorizedException {
        Server.userDAO.createUser(existingUser);
        Assertions.assertThrows(UnauthorizedException.class, () -> {UserService.login(new LoginRequest("username", "diffpassword"));});
    }
}
