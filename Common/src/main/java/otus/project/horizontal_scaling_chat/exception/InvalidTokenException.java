package otus.project.horizontal_scaling_chat.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Auth token is invalid");
    }
}
