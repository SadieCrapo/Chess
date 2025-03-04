package service;

import dataaccess.*;
import dataaccess.MemoryUserDAO.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import server.Server;

import java.util.HashMap;

public class ClearServiceTests {
    UserData existingUser = new UserData("username", "password", "email");
    GameData existingGame = new GameData(1, "white", "black", "game", null);
    AuthData existingAuth = new AuthData("auth", "name");

    @BeforeEach
    public void setup() {
        Server.userDAO = new MemoryUserDAO();
        Server.gameDAO = new MemoryGameDAO();
        Server.authDAO = new MemoryAuthDAO();

    }

    @Test
    @DisplayName("Successful Clear")
    public void successClear() throws UnauthorizedException {
        Server.userDAO.createUser(existingUser);
        Server.gameDAO.createGame(existingGame);
        Server.gameDAO.createGame(new GameData(2, "white", "black", "game", null));
        Server.gameDAO.createGame(new GameData(3, "white", "black", "game", null));
        Server.authDAO.createAuth(existingAuth);
        Server.authDAO.createAuth(new AuthData("data", "user"));

        ClearResult result = ClearService.clear();

        Assertions.assertNull(Server.userDAO.getUser(existingUser.username()));
        Assertions.assertNull(Server.gameDAO.getGame(existingGame.gameID()));
        Assertions.assertNull(Server.authDAO.getAuth(existingAuth.authToken()));
    }
}
