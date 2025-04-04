package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTests {
    private static SQLUserDAO db;
    private UserData testUser = new UserData("testUser", "testPassword", "testEmail");
    private UserData initialUser = new UserData("initialUser", "initialPassword", "initialEmail");

    @BeforeAll
    public static void initializeDatabase() throws DataAccessException {
        db = new SQLUserDAO();
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        db.createUser(initialUser);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.clear();
    }

    @Test
    @DisplayName("Clear Test")
    public void clearTest() throws DataAccessException {
        assertDoesNotThrow(() -> db.getUser("initialUser"));
        db.clear();
        assertNull(db.getUser("initialUser"));
    }

    @Test
    @DisplayName("Successful Create User")
    public void successCreate() {
        assertDoesNotThrow(() -> db.createUser(testUser));
    }

    @Test
    @DisplayName("Fail to Create User")
    public void failCreate() {
        assertThrows(DataAccessException.class, () -> db.createUser(initialUser));
    }

    @Test
    @DisplayName("Successful Get User")
    public void successGet() throws DataAccessException {
        db.createUser(testUser);
        UserData result = db.getUser("testUser");
        assertEquals(testUser.username(), result.username());
        assertEquals(testUser.email(), result.email());
        assertTrue(BCrypt.checkpw(testUser.password(), result.password()));
    }

    @Test
    @DisplayName("Fail to Get User")
    public void failGet() throws DataAccessException {
        assertNull(db.getUser("testUser"));
    }
}
