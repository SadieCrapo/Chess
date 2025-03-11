package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import result.ListResult;
import server.Server;
import service.GameService;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {

    private static SQLGameDAO db;
    private GameData testGame = new GameData(0, "white", null, "gameName", new ChessGame());

//    public void createDatabase() throws SQLException, DataAccessException {
//        db = new SQLGameDAO();
//    }

    @BeforeAll
    public static void initializeDatabase() throws SQLException, DataAccessException {
        db = new SQLGameDAO();
//        createDatabase();
    }

//    @BeforeEach
//    public void setup() throws SQLException, DataAccessException {
////        db = new SQLGameDAO();
//    }

    @Test
    @DisplayName("Successful Create Game")
    public void successCreate() {
        assertDoesNotThrow(() -> db.createGame(testGame));
    }

    @Test
    @DisplayName("Fail Create Game because Name too long")
    public void failCreate() throws DataAccessException {
        String superLongString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        GameData game = new GameData(0,"white", null, superLongString, null);
        assertThrows(DataAccessException.class, () -> db.createGame(game));
    }

    @Test
    @DisplayName("Successful Get Game")
    public void successGet() throws DataAccessException {
        int gameID = db.createGame(testGame);
        GameData newGame = db.getGame(gameID);
        assertEquals(testGame.gameID(), newGame.gameID());
        assertEquals(testGame.whiteUsername(), newGame.whiteUsername());
        assertEquals(testGame.blackUsername(), newGame.blackUsername());
        assertEquals(testGame.gameName(), newGame.gameName());
        assertEquals(testGame.game(), newGame.game());
    }

    @Test
    @DisplayName("Fail Get Game because Wrong gameID")
    public void failGet() {
        assertThrows(DataAccessException.class, () -> db.getGame(-1));
    }

//    @Test
//    @DisplayName("Clear Test")
//    public void clearTest() {
//        Server.gameDAO.createGame(existingGame);
//        ListResult result = GameService.listGames();
//
//        ArrayList<GameData> testList = new ArrayList<>();
//        testList.add(existingGame);
//
//        Assertions.assertEquals(testList, result.games());
//    }
}
