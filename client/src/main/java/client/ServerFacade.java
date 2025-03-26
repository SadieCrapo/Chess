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

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws BadRequestException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
//            throw new IOException(ex.getMessage());
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

//    public static ResponseException fromJson(InputStream stream) {
//        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
//        var status = ((Double)map.get("status")).intValue();
//        String message = map.get("message").toString();
//        return new ResponseException(status, message);
//    }

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
