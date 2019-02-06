package io.gr1d.core.util;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Markers to be used on logs to separate some responsibilities.
 */
public interface Markers {

    /**
     * This marker serves to notify the administrator by Email or another
     * notification service that some event has occurred.
     *
     * Use this to receive notifications whenever this log is called.
     */
    Marker NOTIFY_ADMIN = MarkerFactory.getMarker("NOTIFY_ADMIN");

}
