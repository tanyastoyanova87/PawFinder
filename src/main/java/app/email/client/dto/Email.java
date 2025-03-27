package app.email.client.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Email {

    private UUID userId;

    private String subject;

    private LocalDateTime createdOn;

    private String status;
}
