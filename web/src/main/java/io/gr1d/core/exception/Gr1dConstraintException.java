package io.gr1d.core.exception;

import lombok.Getter;

@Getter
public class Gr1dConstraintException extends RuntimeException {

    private final String property;
    private final String message;
    private final boolean translate;

    public Gr1dConstraintException(final String property, final String message) {
        this(property, message, true);
    }

    public Gr1dConstraintException(final String property, final String message, boolean translate) {
        this.property = property;
        this.message = message;
        this.translate = translate;
    }

}
