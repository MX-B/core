package io.gr1d.core.strategy;

import io.gr1d.core.Gr1dCoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Gr1dCoreApplication.class)
public class StrategyResolverTest {

    @Autowired
    private StrategyResolver resolver;

    @Test
    public void testResolveStrategy() {
        final LaughStrategy laughStrategy = resolver.resolve(LaughStrategy.class, "LOL");
        assertThat(laughStrategy.laugh("Here`s some very silly joke")).isEqualTo("LOL");

        final LaughStrategy anotherLaughStrategy = resolver.resolve(LaughStrategy.class, "ROFL");
        assertThat(anotherLaughStrategy.laugh("Here`s some very silly joke")).isEqualTo("ROFL");

        final LaughStrategy notFoundLaughStrategy = resolver.resolve(LaughStrategy.class, "LMAO");
        assertThat(notFoundLaughStrategy).isNull();
    }

}
