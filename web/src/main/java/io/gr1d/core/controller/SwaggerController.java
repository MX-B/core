package io.gr1d.core.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
public class SwaggerController {

    @RequestMapping(method = RequestMethod.GET, path = "")
    public void swagger(final HttpServletResponse response) throws IOException {
        try (final InputStream stream = getClass().getClassLoader().getResourceAsStream("swagger-ui.html")) {
            IOUtils.copy(stream, response.getOutputStream());
        }
    }
}
