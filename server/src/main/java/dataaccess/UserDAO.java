package dataaccess;

import model.UserData;
import service.UnauthorizedException;

public interface UserDAO {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
