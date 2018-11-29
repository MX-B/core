package io.gr1d.core.controller;

import io.gr1d.core.TestUtils;
import io.gr1d.core.response.Gr1dError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CustomErrorControllerTest {
	
	private CustomErrorController controller;
	
	@Mock
	private ErrorAttributes errorAttributes;
	
	@Mock
	private MessageSource messageSource;
	
	@Mock
	private HttpServletRequest request;
	
	@Before
	public void init() {
		initMocks(this);
		controller = new CustomErrorController("/test", errorAttributes, messageSource);
	}
	
	@Test
	public void error() {
		final Map<String, Object> attribs = new HashMap<>(4);
		attribs.put("error", "test");
		attribs.put("message", "test");
		attribs.put("trace", "test");
		attribs.put("status", 999);
		
		when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");
		when(errorAttributes.getErrorAttributes(any(), anyBoolean())).thenReturn(attribs);
		
		final ResponseEntity<Collection<Gr1dError>> result = controller.error(request, null, Locale.getDefault());
		final Gr1dError error = result.getBody().iterator().next();
		
		assertNotNull(result);
		assertEquals(NOT_FOUND, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(1, result.getBody().size());
		assertEquals("test", error.getError());
		assertNull(error.getMeta());
		assertEquals("__test__", error.getMessage());
	}
	
	@Test
	public void errorWithCustomStatus() {
		final Map<String, Object> attribs = new HashMap<>(4);
		attribs.put("error", "test");
		attribs.put("message", "test");
		attribs.put("trace", "test");
		attribs.put("status", I_AM_A_TEAPOT.value());
		
		when(messageSource.getMessage(anyString(), any(Object[].class), eq(Locale.getDefault()))).thenReturn("__test__");
		when(errorAttributes.getErrorAttributes(any(), anyBoolean())).thenReturn(attribs);
		
		final ResponseEntity<Collection<Gr1dError>> result = controller.error(request, null, Locale.getDefault());
		final Gr1dError error = result.getBody().iterator().next();
		
		assertNotNull(result);
		assertEquals(I_AM_A_TEAPOT, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(1, result.getBody().size());
		assertEquals("test", error.getError());
		assertNull(error.getMeta());
		assertEquals("__test__", error.getMessage());
	}
	
	@Test
	public void testAnnotations() {
		TestUtils.testAnnotations(CustomErrorController.class, RestController.class);
	}
	
	@Test
	public void testLogger() throws Exception {
		TestUtils.testLogger(CustomErrorController.class);
	}
}
