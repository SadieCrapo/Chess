package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    void createAuth(AuthData);
    AuthData getAuth(AuthToken);
//    void updateAuth(AuthData);
    void deleteAuth(AuthToken); // or maybe should return a boolean??
}
