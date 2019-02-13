package io.gr1d.core.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

@RestController
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
public class SwaggerController {

    private String swaggerUi;

    @RequestMapping(method = RequestMethod.GET, path = "")
    public void swagger(final HttpServletResponse response) throws IOException {
        if (swaggerUi == null) {
            getSwaggerUi();
        }
        response.getWriter().append(swaggerUi);
    }

    private void getSwaggerUi() throws IOException {
        try (final StringWriter writer = new StringWriter()) {
            final URL url = new URL("https://swagger-innovation-cloud.firebaseapp.com");
            final URLConnection connection = url.openConnection();
            IOUtils.copy(connection.getInputStream(), writer, Charset.forName("UTF-8"));
            swaggerUi = writer.toString().replace("https://petstore.swagger.io/v2/swagger.json", "/v2/api-docs");
            swaggerUi = swaggerUi.replaceAll("=\"\\.?/?assets", "=\"https://swagger-innovation-cloud.firebaseapp.com/assets");
        }
    }
}
