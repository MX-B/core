package io.gr1d.core.documentation;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import io.gr1d.core.controller.CustomErrorController;
import io.gr1d.core.documentation.config.SwaggerInfo;
import io.gr1d.core.documentation.config.SwaggerSecuritySchema;
import io.gr1d.core.documentation.enums.SecurityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@Slf4j
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
public class SwaggerConfig implements WebMvcConfigurer {

    @Autowired
    private SwaggerInfo info;

    @Autowired
    private SwaggerSecuritySchema security;

    /**
     * Configures the Servlet Bean do Swagger
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket createSwaggerApi() {
        if (log.isTraceEnabled()) {
            log.trace("function=createSwaggerApi status=init");
        }
        final Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(getAPIs())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(info.getApiInfo())
                .useDefaultResponseMessages(false);

        if (!SecurityType.NONE.equals(security.getSecurityType())) {
            docket.securitySchemes(Collections.singletonList(security.getSecuritySchema()));
        }
        log.trace("function=createSwaggerApi status=done");
        return docket;
    }

    /**
     * Ignore Spring Error Controller
     *
     * @return {@link Predicate}
     */
    private Predicate<RequestHandler> getAPIs() {
        log.trace("function=getAPIs status=init");
        final String pack = CustomErrorController.class.getPackage().toString().split(" ")[1];
        final Predicate<RequestHandler> predicate = Predicates.not(RequestHandlerSelectors.basePackage(pack));
        log.trace("function=getAPIs status=done");
        return predicate;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
