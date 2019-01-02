package io.gr1d.core.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@Profile("!test")
public class SendgridEmailService implements EmailService {

    private final SendGrid sendGrid;
    private final ObjectMapper objectMapper;

    @Autowired
    public SendgridEmailService(final SendGrid sendGrid, final ObjectMapper objectMapper) {
        this.sendGrid = sendGrid;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends an e-mail using a Sendgrid Template.
     *
     * The {@code context} will be injected in the request as a {@code dynamic_template_data} field
     */
    public void send(final Email email) {
        try {
            log.info("Sending Sendgrid email from {}, to {} - subject={}, template={}, context={}",
                    email.getFrom(), email.getTo(), email.getSubject(), email.getTemplate(), email.getContext());

            final com.sendgrid.Email emailFrom = new com.sendgrid.Email(email.getFrom());
            final com.sendgrid.Email emailTo = new com.sendgrid.Email(email.getTo());
            final Mail mail = new Mail();
            mail.setFrom(emailFrom);
            mail.setSubject(email.getSubject());
            mail.setTemplateId(email.getTemplate());

            final Personalization personalization = new Personalization();
            personalization.addTo(emailTo);
            mail.addPersonalization(personalization);

            email.getAttachments().forEach(item -> {
                final Attachments attachment = new Attachments();
                attachment.setDisposition("attachment");
                attachment.setContent(item.getBase64());
                attachment.setContentId(item.getId());
                attachment.setFilename(item.getFileName());
                attachment.setType(item.getMimeType());
                mail.addAttachments(attachment);
            });

            final String prePayload = mail.build();

            final String str = "\"personalizations\":[{";
            final int start = prePayload.indexOf(str) + str.length();

            final StringBuilder finalPayload = new StringBuilder();
            finalPayload.append(prePayload.substring(0, start));
            finalPayload.append("\"dynamic_template_data\":");
            finalPayload.append(objectMapper.writeValueAsString(email.getContext()));
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

}
