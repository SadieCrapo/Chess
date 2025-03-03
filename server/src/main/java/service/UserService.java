package service;

import dataaccess.MemoryUserDAO;
import model.*;
import request.*;
import result.*;
import server.Server;

import java.util.Objects;
import java.util.UUID;

public class UserService {

//    public RegisterResult register(RegisterRequest request) {
//
//    }

    public LoginResult login(LoginRequest request) throws UnauthorizedException {
        String username = request.username();
        UserData user = Server.userDAO.getUser(username);
        if (!Objects.equals(user.password(), request.password())) {
            throw new UnauthorizedException("Password does not match");
        }

        String token = createAuthToken();
        Server.authDAO.createAuth(new AuthData(token, username));

        return new LoginResult(username, token);
    }

//    public void logout(LogoutRequest request) {
//
//    }

    private String createAuthToken() {
        return UUID.randomUUID().toString();
    }
}
