package io.gr1d.core.email;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an Email representation
 * To send the email, first configure destination (to)
 * Template and use method send()
 *
 * @author Sérgio Marcelino
 */
public class Email {
    private final EmailService emailService;

    private String from;
    private String to;
    private String subject;
    private String template;
    private Map<String, Object> context;

    Email(final EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sets the template to send the email
     *
     * @param template key representation
     */
    public Email template(final String template) {
        this.template = template;
        return this;
    }

    /**
     * Adds a new variable to the context
     *
     * @param key the key to be used inside template
     * @param value the value to show when the template processes
     */
    public Email add(final String key, final Object value) {
        if (context == null) {
            context = new HashMap<>();
        }

        context.put(key, value);

        return this;
    }

    /**
     * Configures the sender to show up in the recipient inbox
     * @param from email address
     */
    public Email from(final String from) {
        this.from = from;
        return this;
    }

    /**
     * Configures the recipient to receive the message
     * @param to email address
     */
    public Email to(final String to) {
        this.to = to;
        return this;
    }

    /**
     * Configures the Subject to send the email message
     * Optional
     *
     * @param subject Email Subject
     */
    public Email subject(final String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Sends the email message
     */
    public Email send() {
        Assert.notNull(from, "Property `from` is required");
        Assert.isTrue(!from.isEmpty(), "Property `from` is required");

        Assert.notNull(to, "Property `to` is required");
        Assert.isTrue(!to.isEmpty(), "Property `to` is required");

        Assert.notNull(template, "Property `template` is required");
        Assert.isTrue(!template.isEmpty(), "Property `template` is required");

        emailService.send(from, to, subject, template, context);
        return this;
    }
}
