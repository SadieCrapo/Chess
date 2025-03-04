package service;

import dataaccess.MemoryUserDAO;
import model.*;
import request.*;
import result.*;
import server.Server;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    public static RegisterResult register(RegisterRequest request) throws UsernameTakenException, BadRequestException {
        String username = request.username();
        if (username == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("All fields must be completed");
        }
        UserData user = new UserData(username, request.password(), request.email());
        if (Server.userDAO.getUser(username) != null) {
            throw new UsernameTakenException("Username has already been claimed");
        }

        Server.userDAO.createUser(user);

        String token = createAuthToken();
        Server.authDAO.createAuth(new AuthData(token, username));

        return new RegisterResult(username, token);
    }

    public static LoginResult login(LoginRequest request) throws UnauthorizedException {
        String username = request.username();
        UserData user = Server.userDAO.getUser(username);
        if (user == null || !Objects.equals(user.password(), request.password())) {
            throw new UnauthorizedException("Password does not match");
        }

        String token = createAuthToken();
        Server.authDAO.createAuth(new AuthData(token, username));

        return new LoginResult(username, token);
    }

    public static LogoutResult logout(LogoutRequest request) throws UnauthorizedException {
        if (Server.authDAO.getAuth(request.authToken()) == null) {
            throw new UnauthorizedException("AuthToken not in db");
        }
        Server.authDAO.deleteAuth(request.authToken());

        return new LogoutResult();
    }

    private static String createAuthToken() {
        return UUID.randomUUID().toString();
    }
}
