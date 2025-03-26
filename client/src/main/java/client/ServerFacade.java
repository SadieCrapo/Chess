package client;

import com.google.gson.Gson;

import exception.BadRequestException;
import request.*;
import result.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult login(LoginRequest request) throws BadRequestException {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public RegisterResult register(RegisterRequest request) throws BadRequestException {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public void logout(String authToken) throws BadRequestException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public CreateResult create(CreateRequest request, String authToken) throws BadRequestException {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateResult.class, authToken);
    }

    public ListResult list(String authToken) throws BadRequestException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListResult.class, authToken);
    }

//    public LogoutResult logout(LogoutRequest request) throws BadRequestException {
//        var path = "/session";
//        try {
//            return this.makeRequest("DELETE", path, request, LogoutResult.class);
//        } catch (BadRequestException e) {
//            throw new BadRequestException(e.getMessage() + "server.logout()");
//        }
//    }

    private <T> T makeRequest(String method, String path, Object request, Class <T> responseClass) throws BadRequestException {
        return makeRequest(method, path, request, responseClass, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws BadRequestException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(authToken, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("Authorization", authToken);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws BadRequestException, IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw BadRequestException.fromJson(respErr);
                }
            }

            throw new BadRequestException("other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
