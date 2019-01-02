package io.gr1d.core.email;

/**
 * Service to use to send emails
 *
 * @author sergio.marcelino
 * @see Email
 */

public interface EmailService {

    /**
     * Sends an e-mail using a Template.
     */
    void send(final Email email);

    /**
     * Use to create a new email message, the email is not sent right away. You must
     * use {@code email.send()}
     *
     * @return a new {@link Email}, use it to configure the message and send
     */
    default Email email() {
        return new Email(this);
    }

}
