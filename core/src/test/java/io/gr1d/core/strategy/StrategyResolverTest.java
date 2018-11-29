package io.gr1d.core.strategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StrategyResolverTest {

    private StrategyResolver resolver;

    @Mock
    private ApplicationContext applicationContext;

    @Before
    public void init() {
        initMocks(this);
        resolver = new StrategyResolver(applicationContext);
    }

    @Test
    public void testResolveStrategy() {
        when(applicationContext.getBean(eq("LaughStrategy.LOL"), same(LaughStrategy.class))).thenReturn(new LolLaughStrategy());
        when(applicationContext.getBean(eq("LaughStrategy.ROFL"), same(LaughStrategy.class))).thenReturn(new RoflLaughStrategy());
        when(applicationContext.getBean(eq("LaughStrategy.LMAO"), same(LaughStrategy.class))).thenThrow(NoSuchBeanDefinitionException.class);

        final LaughStrategy laughStrategy = resolver.resolve(LaughStrategy.class, "LOL");
        assertThat(laughStrategy.laugh("Here`s some very silly joke")).isEqualTo("LOL");

        final LaughStrategy anotherLaughStrategy = resolver.resolve(LaughStrategy.class, "ROFL");
        assertThat(anotherLaughStrategy.laugh("Here`s some very silly joke")).isEqualTo("ROFL");

        final LaughStrategy notFoundLaughStrategy = resolver.resolve(LaughStrategy.class, "LMAO");
        assertThat(notFoundLaughStrategy).isNull();
    }

}
