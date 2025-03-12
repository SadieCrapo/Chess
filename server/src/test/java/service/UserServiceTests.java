package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import server.Server;

public class UserServiceTests {
    UserData existingUser = new UserData("username", "password", "email");

    @BeforeEach
    public void setup() {
        Server.userDAO = new MemoryUserDAO();
        Server.gameDAO = new MemoryGameDAO();
        Server.authDAO = new MemoryAuthDAO();
    }

    @Test
    @DisplayName("Successful Login")
    public void successLogin() throws UnauthorizedException, DataAccessException {
        Server.userDAO.createUser(existingUser);
        LoginResult result = UserService.login(new LoginRequest("username", "password"));

        Assertions.assertEquals(existingUser.username(), result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Failed Login because no Username in db")
    public void failLoginUsername() {
        Assertions.assertThrows(UnauthorizedException.class, () -> {UserService.login(new LoginRequest("diffname", "diffpassword"));});
    }

    @Test
    @DisplayName("Failed Login because Wrong Password")
    public void failLoginPassword() throws DataAccessException {
        Server.userDAO.createUser(existingUser);
        Assertions.assertThrows(UnauthorizedException.class, () -> {UserService.login(new LoginRequest("username", "diffpassword"));});
    }

    @Test
    @DisplayName("Successful Registration")
    public void successRegister() throws UsernameTakenException, BadRequestException, DataAccessException {
        RegisterResult result = UserService.register(new RegisterRequest("username", "password", "email"));

        Assertions.assertEquals("username", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Failed Register because Username already in db")
    public void failRegisterUsername() throws DataAccessException {
        Server.userDAO.createUser(existingUser);
        Assertions.assertThrows(UsernameTakenException.class, () -> {UserService.register(new RegisterRequest("username", "password", "email"));});
    }

    @Test
    @DisplayName("Successful Logout")
    public void successLogout() throws UnauthorizedException, DataAccessException {
        Server.userDAO.createUser(existingUser);
        Server.authDAO.createAuth(new AuthData("authToken", "username"));

        UserService.logout(new LogoutRequest("authToken"));

        Assertions.assertNull(Server.authDAO.getAuth("authToken"));
    }

    @Test
    @DisplayName("Failed Logout because no AuthToken in db")
    public void failLogoutNoAuth() {
        Assertions.assertThrows(Exception.class, () -> {UserService.logout(new LogoutRequest("authToken"));});
    }
}
