package nz.co.kunal.tokenization.exception;

public class ValidationFailedException extends RuntimeException {

    public ValidationFailedException(final String message) {
        super(message);
    }
}
