package service;

import dataaccess.DataAccessException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import request.*;
import result.*;
import server.Server;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    public static RegisterResult register(RegisterRequest request) throws UsernameTakenException, BadRequestException, DataAccessException {
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

    public static LoginResult login(LoginRequest request) throws UnauthorizedException, DataAccessException {
        String username = request.username();
        UserData user = Server.userDAO.getUser(username);
        if (user == null || !BCrypt.checkpw(request.password(), user.password())) {
            throw new UnauthorizedException("Password does not match");
        }

        String token = createAuthToken();
        Server.authDAO.createAuth(new AuthData(token, username));

        return new LoginResult(username, token);
    }

    public static LogoutResult logout(LogoutRequest request) throws UnauthorizedException, DataAccessException {
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

//$2a$10$Rlt1SOEPn4TIU/Cl1D/CbONqdvFnOVHpaPcw0mozzC.DwuE73ibPS
//$2a$10$P.T4qwkne20/Qw6fP37IoO4FPVmCV1/Djs/q0aKxmF7RBHWs4vPIS
//$2a$10$MGXksZIUn2RsqvrT3Zqea.K.0U5Kw.xzoIg5d0A96o3V8Hz2pKvdu
//$2a$10$ScwkniIWOwhL.hdihPqQFOKQC2v79ORo/aeA0RmgmhMHovDfJbfUK