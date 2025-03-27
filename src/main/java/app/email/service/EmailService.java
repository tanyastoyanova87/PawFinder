package app.email.service;

import app.email.client.EmailClient;
import app.email.client.dto.Email;
import app.email.client.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = emailClient.sendEmail(emailRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.warn("[Feign call to notification-svc failed] Can't send email to user with id [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Can't send email to user with id [%s]".formatted(userId));

        }
    }

    public List<Email> getAllEmails(UUID userId) {
        ResponseEntity<List<Email>> httpResponse = emailClient.getEmails(userId);

        return httpResponse.getBody();
    }
}
