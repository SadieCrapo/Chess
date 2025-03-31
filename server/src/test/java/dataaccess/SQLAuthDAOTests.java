package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import exception.UnauthorizedException;

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
    public void clearTest() throws DataAccessException, UnauthorizedException {
        assertDoesNotThrow(() -> db.getAuth("initialAuth"));
        db.clear();
        assertNull(db.getAuth("initialAuth"));
    }

    @Test
    @DisplayName("Successful Create Auth")
    public void successCreate() {
        assertDoesNotThrow(() -> db.createAuth(testAuth));
    }

    @Test
    @DisplayName("Fail to Create Auth")
    public void failCreate() {
        assertThrows(DataAccessException.class, () -> db.createAuth(initialAuth));
    }

    @Test
    @DisplayName("Successful Get Auth")
    public void successGet() throws UnauthorizedException, DataAccessException {
        db.createAuth(testAuth);
        assertEquals(testAuth, db.getAuth("testAuth"));
    }

    @Test
    @DisplayName("Fail to Get Auth")
    public void failGet() throws UnauthorizedException, DataAccessException {
        assertNull(db.getAuth("doesn't exist"));
    }

    @Test
    @DisplayName("Successful Delete Auth")
    public void successDelete() throws UnauthorizedException, DataAccessException {
        assertDoesNotThrow(() -> db.deleteAuth("initialAuth"));
        assertNull(db.getAuth("initialAuth"));
    }

    @Test
    @DisplayName("Fail to Delete Auth")
    public void failDelete() {
        assertThrows(DataAccessException.class, () -> db.deleteAuth("doesn't exist"));
    }
}
