package dataaccess;

import model.UserData;

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
        userDatabase.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return userDatabase.get(username);
    }
}
