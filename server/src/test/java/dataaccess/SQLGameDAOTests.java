package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTests {

    private static SQLGameDAO db;
    private GameData testGame = new GameData(0, "white", null, "gameName", new ChessGame());
    int initialID;

    @BeforeAll
    public static void initializeDatabase() throws SQLException, DataAccessException {
        db = new SQLGameDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        initialID = db.createGame(new GameData(0, null, "initialBlack", "initialName", new ChessGame()));
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.clear();
    }

    @Test
    @DisplayName("Successful Create Game")
    public void successCreate() {
        assertDoesNotThrow(() -> db.createGame(testGame));
    }

    @Test
    @DisplayName("Fail Create Game because Name too long")
    public void failCreate() throws DataAccessException {
        String superLongString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        GameData game = new GameData(0,"white", null, superLongString, null);
        assertThrows(DataAccessException.class, () -> db.createGame(game));
    }

    @Test
    @DisplayName("Successful Get Game")
    public void successGet() throws DataAccessException {
        int gameID = db.createGame(testGame);
        GameData newGame = db.getGame(gameID);
        assertEquals(newGame, testGame);
        assertDoesNotThrow(() -> db.getGame(initialID));
    }

    @Test
    @DisplayName("Fail Get Game because Wrong gameID")
    public void failGet() throws DataAccessException {
        assertNull(db.getGame(-1));
    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {
        int gameID = db.createGame(testGame);
        db.clear();
        assertNull(db.getGame(gameID));
        assertEquals(0, db.listGames().size());
    }

    @Test
    @DisplayName("Successful List Games")
    public void successList() throws DataAccessException {
        ArrayList<GameData> resultList = new ArrayList<>();
        resultList.add(db.getGame(initialID));
        assertEquals(resultList, db.listGames());
        resultList.add(testGame);
        db.createGame(testGame);
        assertEquals(resultList, db.listGames());
    }

    @Test
    @DisplayName("List Games when Empty")
    public void emptyList() throws DataAccessException {
        db.clear();
        assertEquals(new ArrayList<GameData>(), db.listGames());
    }

    @Test
    @DisplayName("Successful Update Game")
    public void successUpdate() throws DataAccessException {
        assertEquals("initialBlack", db.getGame(initialID).blackUsername());
        assertNull(db.getGame(initialID).whiteUsername());
        GameData newGame = new GameData(initialID, "white", "initialBlack", "initialName", new ChessGame());
        assertDoesNotThrow(() -> db.updateGame(newGame));
        assertEquals("white", db.getGame(initialID).whiteUsername());
    }

    @Test
    @DisplayName("Fail Update Game because gameID not in db")
    public void failUpdateID() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> db.updateGame(new GameData(-1, "white", "black", "gameName", new ChessGame())));
    }
}
