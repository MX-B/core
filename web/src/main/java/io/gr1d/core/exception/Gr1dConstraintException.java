package io.gr1d.core.exception;

import lombok.Getter;

@Getter
public class Gr1dConstraintException extends RuntimeException {

    private final String property;
    private final String message;

    public Gr1dConstraintException(final String property, final String message) {
        this.property = property;
        this.message = message;
    }

}
