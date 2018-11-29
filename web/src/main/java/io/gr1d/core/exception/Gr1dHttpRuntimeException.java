package io.gr1d.core.exception;

import lombok.Getter;

/**
 * Basic HTTP Exception to generate an HTTP error
 */
@Getter
public class Gr1dHttpRuntimeException extends RuntimeException {

    /**
     * Will be the HTTP status returned
     */
    private final int httpStatus;

    /**
     * This message will be i18n and printed in the body
     */
    private final String message;

    /**
     * Arguments will be used in the i18n process, it is optional
     */
    private final Object[] arguments;

    public Gr1dHttpRuntimeException(final int httpStatus, final String message, final Object[] arguments) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.arguments = arguments;
    }

    public Gr1dHttpRuntimeException(final int httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.arguments = new Object[] {};
    }

}
