package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
//    void updateUser(UserData user);
    void deleteUser(String username); // or maybe this should return a boolean??
}
