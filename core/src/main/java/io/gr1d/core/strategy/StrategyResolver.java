package io.gr1d.core.strategy;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Component used to resolve strategies
 *
 * @author Sérgio Marcelino
 */
@Component
public class StrategyResolver {

    private final ApplicationContext applicationContext;

    public StrategyResolver(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Will lookup for an implementation of the given Strategy and return
     *
     * @param clazz Strategy Interface
     * @param name The strategy implementation name
     * @param <T> Strategy Interface Type
     * @return null if no strategy was found for the given name
     */
    public <T> T resolve(final Class<T> clazz, final String name) {
        try {
            return applicationContext.getBean(String.format("%s.%s", clazz.getSimpleName(), name), clazz);
        } catch (final NoSuchBeanDefinitionException ex) {
            return null;
        }
    }

}
