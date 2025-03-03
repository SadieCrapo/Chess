package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authDatabase;

    public MemoryAuthDAO() {
        authDatabase = new HashMap<>();
    }

    @Override
    public void clear() {
        authDatabase.clear();
    }

    @Override
    public void createAuth(AuthData auth) {
        authDatabase.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDatabase.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDatabase.remove(authToken);
    }
}
