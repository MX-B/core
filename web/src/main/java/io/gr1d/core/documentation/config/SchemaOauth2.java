package io.gr1d.core.documentation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.*;

import java.util.Arrays;
import java.util.List;

/**
 * OAuth2 Authentication schema representation
 *
 * Uses properties:
 * `swagger.security.oauth2.name`: Name of the security scheme and cannot be empty or null
 * `swagger.security.oauth2.clientId`: OAuth2 Client ID, cannot be empty or null
 * `swagger.security.oauth2.clientSecret`: OAuth2 Client Secret, cannot be empty or null
 * `swagger.security.oauth2.authorizeEndpointUrl.name`: OAuth2 endpoint URL to authorize, cannot be empty or null
 * `swagger.security.oauth2.tokenEndpointUrl`: OAuth2 Token endpoint URL, cannot be empty or null
 *
 * @author Efraim Coutinho
 */
@Getter
@Setter
public class SchemaOauth2 implements SchemaInterface {

    private String name;
    private String clientId;
    private String clientSecret;
    private String authorizeEndpointUrl;
    private String tokenEndpointUrl;

    @Override
    public SecurityScheme getSecurityScheme() {
        validate();
        final TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(authorizeEndpointUrl, clientId, clientSecret);
        final TokenEndpoint tokenEndpoint = new TokenEndpoint(tokenEndpointUrl, "token");
        final List<GrantType> grantTypes = Arrays.asList(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return new OAuthBuilder()
                .name(name)
                .grantTypes(grantTypes)
                .build();
    }

    private void validate() {
        Assert.notNull(name, "Property 'swagger.security.oauth2.name' cannot be null");
        Assert.isTrue(!name.isEmpty(), "Property 'swagger.security.basic.name' cannot be empty");

        Assert.notNull(clientId, "Property 'swagger.security.oauth2.clientId' cannot be null");
        Assert.isTrue(!clientId.isEmpty(), "Property 'swagger.security.basic.clientId' cannot be empty");

        Assert.notNull(clientSecret, "Property 'swagger.security.oauth2.clientSecret' cannot be null");
        Assert.isTrue(!clientSecret.isEmpty(), "Property 'swagger.security.oauth2.clientSecret' cannot be empty");

        Assert.notNull(authorizeEndpointUrl, "Property 'swagger.security.oauth2.authorizeEndpointUrl' cannot be null");
        Assert.isTrue(!authorizeEndpointUrl.isEmpty(), "Property 'swagger.security.oauth2.authorizeEndpointUrl.name' cannot be empty");

        Assert.notNull(tokenEndpointUrl, "Property 'swagger.security.oauth2.tokenEndpointUrl' cannot be null");
        Assert.isTrue(!tokenEndpointUrl.isEmpty(), "Property 'swagger.security.oauth2.tokenEndpointUrl' cannot be empty");
    }
}