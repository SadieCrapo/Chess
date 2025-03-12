package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, UserData> userDatabase;

    public MemoryUserDAO() {
        userDatabase = new HashMap<>();
    }

    @Override
    public void clear() {
        userDatabase.clear();
    }

    @Override
    public void createUser(UserData user) {
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        userDatabase.put(user.username(), new UserData(user.username(), hashedPassword, user.email()));
    }

    @Override
    public UserData getUser(String username) {
        return userDatabase.get(username);
    }
}
