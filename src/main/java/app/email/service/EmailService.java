package app.email.service;

import app.email.client.EmailClient;
import app.email.client.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EmailService {

    private final EmailClient emailClient;

    @Autowired
    public EmailService(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    public void sendEmail(UUID userId, String subjectEmail, String bodyEmail, String email, String sender) {
        EmailRequest emailRequest = EmailRequest.builder()
                .userId(userId)
                .subject(subjectEmail)
                .body(bodyEmail)
                .email(email)
                .sender(sender)
                .build();

        ResponseEntity<Void> httpResponse = emailClient.sendEmail(emailRequest);

        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Email wasn't send.");
        }
    }
}
