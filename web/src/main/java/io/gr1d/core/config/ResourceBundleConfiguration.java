package io.gr1d.core.config;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.hibernate.validator.messageinterpolation.AbstractMessageInterpolator.USER_VALIDATION_MESSAGES;

/**
 * This configuration is created to merge default ValidationMessage properties
 * with project's specific properties.
 *
 * @author Sérgio Marcelino
 */
@Configuration
public class ResourceBundleConfiguration {

    @Bean
    public LocalValidatorFactoryBean validator() {
        final PlatformResourceBundleLocator resourceBundleLocator = new PlatformResourceBundleLocator(USER_VALIDATION_MESSAGES, null, true);
        final LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();

        factoryBean.setMessageInterpolator(new ResourceBundleMessageInterpolator(resourceBundleLocator));
        return factoryBean;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
