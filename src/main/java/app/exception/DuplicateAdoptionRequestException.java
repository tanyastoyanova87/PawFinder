package app.exception;

public class DuplicateAdoptionRequestException extends RuntimeException {

    public DuplicateAdoptionRequestException() {
    }

    public DuplicateAdoptionRequestException(String message) {
        super(message);
    }
}
