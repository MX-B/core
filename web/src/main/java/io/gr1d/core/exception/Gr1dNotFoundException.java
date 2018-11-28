package io.gr1d.core.exception;

/**
 * Exception to generate a 404 NOT_FOUND HTTP error
 */
public class Gr1dNotFoundException extends Gr1dHttpException {

    public Gr1dNotFoundException(final String message, final String[] arguments) {
        super(404, message, arguments);
    }

    public Gr1dNotFoundException(final String message) {
        super(404, message);
    }
}
