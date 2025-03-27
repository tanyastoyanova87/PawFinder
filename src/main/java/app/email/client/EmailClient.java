package app.email.client;

import app.email.client.dto.Email;
import app.email.client.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "EmailSender", url = "http://localhost:8081/api/v1/emails")
public interface EmailClient {

    @PostMapping
    ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest);

    @GetMapping
    ResponseEntity<List<Email>> getEmails(@RequestParam(name = "userId") UUID userId);
}
