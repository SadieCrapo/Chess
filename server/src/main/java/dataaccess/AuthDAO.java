package dataaccess;

import model.AuthData;
import exception.UnauthorizedException;

public interface AuthDAO {
    void clear() throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException, UnauthorizedException;
    void deleteAuth(String authToken) throws DataAccessException;
}
