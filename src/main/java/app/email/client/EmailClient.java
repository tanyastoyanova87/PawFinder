package app.email.client;

import app.email.client.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EmailSender", url = "http://localhost:8081/api/v1/emails")
public interface EmailClient {

    @PostMapping
    ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest);
}
