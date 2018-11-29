package io.gr1d.core.controller;

import io.gr1d.core.exception.Gr1dHttpException;
import io.gr1d.core.exception.Gr1dHttpRuntimeException;
import io.gr1d.core.response.Gr1dError;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Basic Error Handling
 *
 * @author Sérgio Marcelino
 */
@Slf4j
@Setter
@RestControllerAdvice
public class BaseController {

    public static final String JSON = MediaType.APPLICATION_JSON_UTF8_VALUE;
    public static final String XML = MediaType.APPLICATION_XML_VALUE;

    @Autowired
    private MessageSource messageSource;

    /**
     * This error is handled from Javax.Validation and
     * converted to Gr1dError
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Collection<Gr1dError>> handleException(final ConstraintViolationException exception, final Locale locale) {
        log.error("ConstraintViolationException while invoking Controller method", exception);

        final Collection<Gr1dError> errors = exception.getConstraintViolations().stream().map(Gr1dError::new)
                .collect(Collectors.toList());

        errors.forEach(err -> err.translateMessage(messageSource, locale));

        return ResponseEntity.unprocessableEntity().body(errors);
    }

    /**
     * The default Gr1d Exception, everything is set and converted
     * into a ResponseEntity
     */
    @ExceptionHandler(Gr1dHttpException.class)
    public ResponseEntity<Collection<Gr1dError>> handleException(final Gr1dHttpException exception, final Locale locale) {
        log.debug("Gr1dHttpException while invoking Controller method", exception);

        final String message = messageSource.getMessage(exception.getMessage(), exception.getArguments(), locale);
        final Collection<Gr1dError> errors = Collections.singletonList(new Gr1dError("ExceptionWithStatus", message));

        return ResponseEntity.status(exception.getHttpStatus()).body(errors);
    }

    /**
     * The default Gr1d Exception, everything is set and converted
     * into a ResponseEntity
     */
    @ExceptionHandler(Gr1dHttpRuntimeException.class)
    public ResponseEntity<Collection<Gr1dError>> handleException(final Gr1dHttpRuntimeException exception, final Locale locale) {
        log.debug("Gr1dHttpException while invoking Controller method", exception);

        final String message = messageSource.getMessage(exception.getMessage(), exception.getArguments(), locale);
        final Collection<Gr1dError> errors = Collections.singletonList(new Gr1dError("ExceptionWithStatus", message));

        return ResponseEntity.status(exception.getHttpStatus()).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Collection<Gr1dError>> handleException(final MethodArgumentNotValidException exception, final Locale locale) {
        log.error("MethodArgumentNotValidException while invoking Controller method", exception);
        final Collection<Gr1dError> errors = exception.getBindingResult().getAllErrors()
                .stream().map(Gr1dError::new)
                .collect(Collectors.toList());
        errors.forEach(err -> err.translateMessage(messageSource, locale));
        return ResponseEntity.unprocessableEntity().body(errors);
    }

    /**
     * Any other exception should be handled as an Internal Server Error
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Collection<Gr1dError>> handleException(final Throwable exception, final Locale locale) {
        log.error("Uncaught exception", exception);
        final Collection<Gr1dError> errors = Collections.singletonList(new Gr1dError(exception));
        errors.forEach(err -> err.translateMessage(messageSource, locale));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
