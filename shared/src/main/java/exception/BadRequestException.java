package exception;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException fromJson(InputStream stream) {
        var map = new Gson().fromJson(new InputStreamReader(stream), HashMap.class);
        String message = map.get("message").toString();
        return new BadRequestException(message);
    }
}
