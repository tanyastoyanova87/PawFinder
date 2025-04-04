package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class Email {

    private UUID userId;

    private String subject;

    private LocalDateTime createdOn;

    private String status;
}
