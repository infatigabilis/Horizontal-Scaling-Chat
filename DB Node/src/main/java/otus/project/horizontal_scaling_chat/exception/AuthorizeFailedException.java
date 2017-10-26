package otus.project.horizontal_scaling_chat.exception;

public class AuthorizeFailedException extends RuntimeException {
    public AuthorizeFailedException() {
        super("Authorize failed");
    }
}
