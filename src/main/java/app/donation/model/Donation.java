package app.donation.model;

import app.creditCard.model.CreditCard;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    private CreditCard creditCard;
}
