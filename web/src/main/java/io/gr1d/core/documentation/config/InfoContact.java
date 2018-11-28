package io.gr1d.core.documentation.config;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.Contact;

/**
 * This model will be used to show contact information in the Swagger
 * @author Efraim Coutinho
 */
@Getter
@Setter
public class InfoContact {

    private String name;
    private String email;
    private String url;

    public Contact getSwaggerContact() {
        return new Contact(name, url, email);
    }
}