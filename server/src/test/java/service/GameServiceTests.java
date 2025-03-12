package service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateRequest;
import request.JoinRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;
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
    public void successList() throws DataAccessException {
        Server.gameDAO.createGame(existingGame);
        ListResult result = GameService.listGames();

        ArrayList<GameData> testList = new ArrayList<>();
        testList.add(existingGame);

        Assertions.assertEquals(testList, result.games());
    }

    @Test
    @DisplayName("Fail List because no games")
    public void failList() throws DataAccessException {
        ListResult result = GameService.listGames();

        Assertions.assertTrue(result.games().isEmpty());
    }

    @Test
    @DisplayName("Successful create game")
    public void successCreate() throws DataAccessException {
        CreateRequest request = new CreateRequest("gameName");
        CreateResult result = new CreateResult(-1);
        try {
            result = GameService.createGame(request);
        } catch (Exception e) {}
        Assertions.assertEquals(0, result.gameID());
        Assertions.assertEquals(Server.gameDAO.getGame(result.gameID()).gameName(), "gameName");
    }

    @Test
    @DisplayName("Fail create game")
    public void failCreate() throws DataAccessException {
        Server.gameDAO.createGame(new GameData(0, "white", "black", "game", null));
        CreateRequest request = new CreateRequest("gameName");
        Assertions.assertThrows(BadRequestException.class, () -> GameService.createGame(request));
    }

    @Test
    @DisplayName("Successfully join game")
    public void successJoin() throws DataAccessException {
        Server.gameDAO.createGame(new GameData(1, "white", null, "game", null));
        JoinRequest request = new JoinRequest("BLACK", 1);
        JoinResult result = new JoinResult();
        Assertions.assertDoesNotThrow(() -> GameService.joinGame(request, "black"));
    }

    @Test
    @DisplayName("Fail join game because wrong gameID")
    public void failJoinID() {
        JoinRequest request = new JoinRequest("BLACK", 1);
        JoinResult result = new JoinResult();
        Assertions.assertThrows(BadRequestException.class, () -> GameService.joinGame(request, "black"));
    }

    @Test
    @DisplayName("Fail join game because wrong username")
    public void failJoinName() throws DataAccessException {
        Server.gameDAO.createGame(new GameData(1, "white", null, "game", null));
        JoinRequest request = new JoinRequest("WHITE", 1);
        JoinResult result = new JoinResult();
        Assertions.assertThrows(UsernameTakenException.class, () -> GameService.joinGame(request, "black"));
    }
}
