package app.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DonationRequest {

    @Min(value = 1, message = "The amount must be above €1.")
    @NotNull(message = "Amount must be specified.")
    private BigDecimal amount;
}
