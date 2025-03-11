package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTests {

    private static SQLAuthDAO db;
    private AuthData testAuth = new AuthData("testAuth", "testName");
    private AuthData initialAuth = new AuthData("initialAuth", "initialName");

    @BeforeAll
    public static void initializeDatabase() throws DataAccessException {
        db = new SQLAuthDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        db.createAuth(initialAuth);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.clear();
    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException{
        assertDoesNotThrow(() -> db.getAuth("initialAuth"));
        db.clear();
        assertThrows(DataAccessException.class, () -> db.getAuth("initialAuth"));
    }

    @Test
    @DisplayName("Successful Create Auth")
    public void successCreate() {
        assertDoesNotThrow(() -> db.createAuth(testAuth));
    }

    @Test
    @DisplayName("Fail to Create Auth")
    public void failCreate() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> db.createAuth(initialAuth));
    }

    @Test
    @DisplayName("Successful Get Auth")
    public void successGet() throws DataAccessException {
        db.createAuth(testAuth);
        assertEquals(testAuth, db.getAuth("testAuth"));
    }

    @Test
    @DisplayName("Fail to Get Auth")
    public void failGet() {
        assertThrows(DataAccessException.class, () -> db.getAuth("doesn't exist"));
    }

//    @Test
//    @DisplayName("Successful Delete Auth")
//    public void successDelete() {
//
//    }
//
//    @Test
//    @DisplayName("Fail to Delete Auth")
//    public void failDelete() {
//
//    }
}
