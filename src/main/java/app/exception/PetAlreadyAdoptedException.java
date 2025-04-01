package app.exception;

public class PetAlreadyAdoptedException extends RuntimeException {

    public PetAlreadyAdoptedException() {
    }

    public PetAlreadyAdoptedException(String message) {
        super(message);
    }
}
