package io.gr1d.core.datasource;

import io.gr1d.core.Gr1dCoreApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@ComponentScan("io.gr1d")
public class SpringTestApplication {
    public static void main(final String[] args) {
        SpringApplication.run(Gr1dCoreApplication.class, args);
    }
}
