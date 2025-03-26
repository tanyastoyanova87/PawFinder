package app.email.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailRequest {

    private UUID userId;

    private String subject;

    private String body;

    private String email;

    private String sender;
}
