package io.gr1d.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.gr1d")
public class Gr1dCoreApplication {
	public static void main(final String[] args) {
		SpringApplication.run(Gr1dCoreApplication.class, args);
	}
}
