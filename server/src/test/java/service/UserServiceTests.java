package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import server.Server;

public class UserServiceTests {
    UserData existingUser = new UserData("username", "password", "email");
    UserData sameUser = new UserData("username", "password", "email");
    UserData differentUser = new UserData("diffname", "diffpassword", "diffemail");

    @BeforeEach
    public void setup() {
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

    @Test
    @DisplayName("Successful Registration")
    public void successRegister() throws UsernameTakenException, BadRequestException {
        RegisterResult result = UserService.register(new RegisterRequest("username", "password", "email"));

        Assertions.assertEquals("username", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    @DisplayName("Failed Register because Username already in db")
    public void failRegisterUsername() throws UsernameTakenException, BadRequestException {
        Server.userDAO.createUser(existingUser);
        Assertions.assertThrows(UsernameTakenException.class, () -> {UserService.register(new RegisterRequest("username", "password", "email"));});
    }

    @Test
    @DisplayName("Successful Logout")
    public void successLogout() throws UnauthorizedException {
        Server.userDAO.createUser(existingUser);
        Server.authDAO.createAuth(new AuthData("authToken", "username"));

//        LogoutResult result = UserService.logout(new LogoutRequest("authToken"));
        UserService.logout(new LogoutRequest("authToken"));

        Assertions.assertNull(Server.authDAO.getAuth("authToken"));
    }

    @Test
    @DisplayName("Failed Logout because no AuthToken in db")
    public void failLogoutNoAuth() throws UnauthorizedException {
//        Server.userDAO.createUser(existingUser);

//        LogoutResult result = UserService.logout(new LogoutRequest("authToken"));

        Assertions.assertThrows(Exception.class, () -> {UserService.logout(new LogoutRequest("authToken"));});
    }
}
