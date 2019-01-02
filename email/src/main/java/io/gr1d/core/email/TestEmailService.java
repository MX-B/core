package io.gr1d.core.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@Profile("test")
public class TestEmailService implements EmailService {

    private final List<Email> sentEmails = new LinkedList<>();

    private final ObjectMapper objectMapper;

    @Autowired
    public TestEmailService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(final Email email) {
        try {
            log.info("Sending new email: {}", objectMapper.writeValueAsString(email));
        } catch (JsonProcessingException e) {
            // do nothing
        }
        sentEmails.add(email);
    }

    public void setup() {
        log.info("Clearing email inbox");
        sentEmails.clear();
    }

    public List<Email> getSentEmails() {
        return Collections.unmodifiableList(sentEmails);
    }

}
