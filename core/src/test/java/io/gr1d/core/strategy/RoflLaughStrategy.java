package io.gr1d.core.strategy;

import org.springframework.stereotype.Component;

@Component("LaughStrategy.ROFL")
public class RoflLaughStrategy implements LaughStrategy {

    @Override
    public String laugh(final String joke) {
        return "ROFL";
    }

}
