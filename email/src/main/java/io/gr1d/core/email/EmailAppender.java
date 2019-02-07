package io.gr1d.core.email;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.gr1d.core.util.Markers;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Component
public class EmailAppender extends AppenderBase<ILoggingEvent> {

    private static final ZoneId UTC = TimeZone.getTimeZone("UTC").toZoneId();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static EmailService emailService;
    private static Boolean enabled;
    private static String from;
    private static String to;
    private static String template;

    public EmailAppender() {
        super();
    }

    @Autowired
    public EmailAppender(final EmailService service,
                         @Value("${io.gr1d.core.email.EmailAppender.enabled:false}") final Boolean notifyEnabled,
                         @Value("${io.gr1d.core.email.EmailAppender.to:}") final String notifyTo,
                         @Value("${io.gr1d.core.email.EmailAppender.from:}") final String notifyFrom,
                         @Value("${io.gr1d.core.email.EmailAppender.template:}") final String notifyTemplate) {
        emailService = service;
        enabled = notifyEnabled;
        to = notifyTo;
        from = notifyFrom;
        template = notifyTemplate;
    }

    @Override
    protected void append(final ILoggingEvent event) {
        if (!this.shouldProcess(event.getMarker())) {
            return;
        }

        event.prepareForDeferredProcessing();

        final String stackTrace = ofNullable(event.getCallerData())
                .map(this::createStackTrace)
                .orElse("");

        final String arguments = ofNullable(event.getArgumentArray())
                .map(this::createArguments)
                .orElse("");

        emailService.email()
                .subject("Critical Error")
                .from(from)
                .to(to)
                .template(template)
                .add("message", event.getMessage())
                .add("level", event.getLevel().toString())
                .add("timestamp", createTimestamp(event.getTimeStamp()))
                .add("stacktrace", stackTrace)
                .add("arguments", arguments)
                .send();
    }

    private String createTimestamp(final long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), UTC).format(FORMATTER) + " UTC";
    }

    private String createStackTrace(final StackTraceElement[] callerData) {
        return Arrays.stream(callerData)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }

    private String createArguments(final Object[] arguments) {
        return Arrays.stream(arguments)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    private boolean shouldProcess(final Marker marker) {
        return enabled && emailService != null && Markers.NOTIFY_ADMIN.equals(marker);
    }

}
