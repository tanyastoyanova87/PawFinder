package app.exception;

import lombok.Getter;

@Getter
public class UsernameAlreadyExistException extends RuntimeException {
    private final String url;

    public UsernameAlreadyExistException(String url) {
        this.url = url;
    }

    public UsernameAlreadyExistException(String message, String url) {
        super(message);
        this.url = url;
    }

}
