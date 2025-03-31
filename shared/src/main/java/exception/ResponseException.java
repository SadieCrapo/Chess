package exception;

public class ResponseException extends Exception {
    private int status;

    public ResponseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
