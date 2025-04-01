package app.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
