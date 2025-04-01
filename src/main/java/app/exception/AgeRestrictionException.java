package app.exception;

import lombok.Getter;

@Getter
public class AgeRestrictionException extends RuntimeException {

    private final String url;

    public AgeRestrictionException(String url) {
        this.url = url;
    }

    public AgeRestrictionException(String message, String url) {
        super(message);
        this.url = url;
    }
}
