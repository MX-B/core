package io.gr1d.core.controller;

import io.gr1d.core.TestUtils;
import io.gr1d.core.exception.Gr1dHttpException;
import io.gr1d.core.datasource.response.Gr1dError;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.*;

public class BaseControllerTest {
    private BaseController baseController;

    @Mock
    private MessageSource messageSource;

    @Before
    public void init() {
        initMocks(this);
        baseController = new BaseController();
        baseController.setMessageSource(messageSource);
    }

    @Test
    public void constraintViolationException() {
        final Set<ConstraintViolation<String>> errors = new HashSet<>(1);
        errors.add(new TestError("testTest"));

        final ConstraintViolationException exception = new ConstraintViolationException("TEST", errors);

        when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");

        final ResponseEntity<Collection<Gr1dError>> result = baseController.handleException(exception, Locale.getDefault());
        final Gr1dError error = result.getBody().iterator().next();

        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()));

        assertNotNull(result);
        assertEquals(UNPROCESSABLE_ENTITY, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("TestError", error.getError());
        assertEquals("test_test", error.getMeta());
        assertEquals("__test__", error.getMessage());
    }

    @Test
    public void methodArgumentNotValidException() throws Exception {
        final MethodParameter param = new MethodParameter(BaseControllerTest.class.getMethod("methodArgumentNotValidException"), -1);
        final BindingResult bindingResult = new BeanPropertyBindingResult("test", "testTest");
        bindingResult.addError(new ObjectError("test", "testTest"));
        final MethodArgumentNotValidException exception = new MethodArgumentNotValidException(param, bindingResult);

        when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");

        final ResponseEntity<Collection<Gr1dError>> result = baseController.handleException(exception, Locale.getDefault());
        final Gr1dError error = result.getBody().iterator().next();

        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()));

        assertNotNull(result);
        assertEquals(UNPROCESSABLE_ENTITY, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("ObjectError", error.getError());
        assertNull(error.getMeta());
        assertEquals("__test__", error.getMessage());
    }

    @Test
    public void throwable() {
        final Exception exception = new Exception("test");

        when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");

        final ResponseEntity<Collection<Gr1dError>> result = baseController.handleException(exception, Locale.getDefault());
        final Gr1dError error = result.getBody().iterator().next();

        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()));

        assertNotNull(result);
        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Exception", error.getError());
        assertNull(error.getMeta());
        assertEquals("__test__", error.getMessage());
    }

    @Test
    public void throwableWithAnnotation() {
        final ExceptionWithStatus exception = new ExceptionWithStatus("test");

        when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");

        System.out.println(exception.getClass().getAnnotation(ResponseStatus.class));

        final ResponseEntity<Collection<Gr1dError>> result = baseController.handleException(exception, Locale.getDefault());
        final Gr1dError error = result.getBody().iterator().next();

        verify(messageSource, times(1)).getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()));

        assertNotNull(result);
        assertEquals(I_AM_A_TEAPOT, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("ExceptionWithStatus", error.getError());
        assertNull(error.getMeta());
        assertEquals("__test__", error.getMessage());
    }

    @Test
    public void testAnnotations() {
        TestUtils.testAnnotations(BaseController.class, RestControllerAdvice.class);
    }

    @Test
    public void testLogger() throws Exception {
        TestUtils.testLogger(BaseController.class);
    }

    private static class ExceptionWithStatus extends Gr1dHttpException {
        public ExceptionWithStatus(final String message) {
            super(I_AM_A_TEAPOT.value(), message);
        }
    }

    private static class TestError implements ConstraintViolation<String> {
        private final String message;

        public TestError(final String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getMessageTemplate() {
            return message;
        }

        @Override
        public String getRootBean() {
            return message;
        }

        @Override
        public Class<String> getRootBeanClass() {
            return String.class;
        }

        @Override
        public Object getLeafBean() {
            return message;
        }

        @Override
        public Object[] getExecutableParameters() {
            return new Object[]{message};
        }

        @Override
        public Object getExecutableReturnValue() {
            return message;
        }

        @Override
        public Path getPropertyPath() {
            return PathImpl.createPathFromString(message);
        }

        @Override
        public Object getInvalidValue() {
            return message;
        }

        @Override
        public ConstraintDescriptor<?> getConstraintDescriptor() {
            return null;
        }

        @Override
        public <U> U unwrap(final Class<U> type) {
            return null;
        }
    }
}
