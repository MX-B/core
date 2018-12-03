package io.gr1d.core.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Implementation to be able to mock LocalDate.now and LocalDateTime.now
 * to make the code more testable
 *
 * @author Sérgio Marcelino
 */
@Component
public class Gr1dClock extends Clock {

    private Clock clock;

    public Gr1dClock() {
        setup();
    }

    public void setClock(final Clock clock) {
        this.clock = clock;
    }

    public void setup() {
        clock = Clock.systemDefaultZone();
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(final ZoneId zone) {
        return clock.withZone(zone);
    }

    @Override
    public Instant instant() {
        return clock.instant();
    }
}
