package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.ListResult;
import result.LoginResult;
import server.Server;

import java.util.ArrayList;

public class GameServiceTests {
    GameData existingGame = new GameData(1, "white", "black", "game", null);

    @BeforeEach
    public void setup() {
        Server.userDAO = new MemoryUserDAO();
        Server.gameDAO = new MemoryGameDAO();
        Server.authDAO = new MemoryAuthDAO();

    }

    @Test
    @DisplayName("Successful List")
    public void successList() {
        Server.gameDAO.createGame(existingGame);
        ListResult result = GameService.listGames();

        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(existingGame);

        Assertions.assertEquals(testList, result.games());
    }

    @Test
    @DisplayName("Fail List because no games")
    public void failList() {
        ListResult result = GameService.listGames();

        Assertions.assertTrue(result.games().isEmpty());
    }
}
