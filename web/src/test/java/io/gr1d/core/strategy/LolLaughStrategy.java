package io.gr1d.core.strategy;

import org.springframework.stereotype.Component;

@Component("LaughStrategy.LOL")
public class LolLaughStrategy implements LaughStrategy {

    @Override
    public String laugh(final String joke) {
        return "LOL";
    }

}
