package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData auth);
    AuthData getAuth(String authToken);
//    void updateAuth(AuthData auth);
    void deleteAuth(String authToken); // or maybe should return a boolean??
}
