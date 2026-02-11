package nz.co.kunal.tokenization.api;

import nz.co.kunal.tokenization.exception.UnknownTokenException;
import nz.co.kunal.tokenization.exception.ValidationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ValidationFailedException.class)
    public ProblemDetail handleValidationFailed(final ValidationFailedException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Failed validation");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(UnknownTokenException.class)
    public ProblemDetail handleNotFound(final UnknownTokenException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Token not found");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAny(final Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Unexpected error");
        pd.setDetail("An unexpected error occurred");
        return pd;
    }
}
