package otus.project.horizontal_scaling_chat.exception;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException() {
        super("Auth token is wrong");
    }
}
