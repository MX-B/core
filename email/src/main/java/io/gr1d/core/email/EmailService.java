package io.gr1d.core.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Service to use to send notifications
 *
 * @author sergio.marcelino
 * @see Email
 */
@Slf4j
@Service
public class EmailService {

    private final SendGrid sendGrid;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmailService(final SendGrid sendGrid, final ObjectMapper objectMapper) {
        this.sendGrid = sendGrid;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends an e-mail using a Sendgrid Template.
     * <p>
     * The {@code context} will be injected in the request as a {@code dynamic_template_data} field
     */
    public void send(final String from, final String to, final String subject, final String template, final Map<String, Object> context) {
        try {
            log.info("Sending Sendgrid email to {} - subject={}, template={}, context={}", to, subject, template, context);

            final com.sendgrid.Email emailFrom = new com.sendgrid.Email(from);
            final com.sendgrid.Email emailTo = new com.sendgrid.Email(to);
            final Mail mail = new Mail();
            mail.setFrom(emailFrom);
            mail.setSubject(subject);
            mail.setTemplateId(template);

            final Personalization personalization = new Personalization();
            personalization.addTo(emailTo);
            mail.addPersonalization(personalization);

            final String prePayload = mail.build();

            final String str = "\"personalizations\":[{";
            final int start = prePayload.indexOf(str) + str.length();

            final StringBuilder finalPayload = new StringBuilder();
            finalPayload.append(prePayload.substring(0, start));
            finalPayload.append("\"dynamic_template_data\":");
            finalPayload.append(objectMapper.writeValueAsString(context));
            finalPayload.append(",");
            finalPayload.append(prePayload.substring(start));

            final String payload = finalPayload.toString();
            log.debug("Sendgrid Mail payload: {}", payload);

            final Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(payload);

            final Response response = sendGrid.api(request);

            if (response.getStatusCode() != 202) {
                log.error("Error sending email - status={}, body={}, headers={}", response.getStatusCode(), response.getBody(), response.getHeaders());
            }
        } catch (final IOException e) {
            log.error("Error sending email", e);
        }
    }

    /**
     * Use to create a new email message, the email is not sent right away. You must
     * use {@code email.send()}
     *
     * @return a new {@link Email}, use it to configure the message and send
     */
    public Email email() {
        return new Email(this);
    }

}
