package io.gr1d.core.documentation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityScheme;

/**
 * Basic Authentication schema representation
 *
 * Uses properties:
 * `swagger.security.basic.name`: Name of the security scheme and cannot be empty or null
 *
 * @author Efraim Coutinho
 */
@Getter
@Setter
public class SchemaBasic implements SchemaInterface {

    private String name;

    @Override
    public SecurityScheme getSecurityScheme() {
        Assert.notNull(name, "Property 'swagger.security.basic.name' cannot be null");
        Assert.isTrue(!name.isEmpty(), "Property 'swagger.security.basic.name' cannot be empty");
        return new BasicAuth(name);
    }
}
