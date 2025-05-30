package app.creditCard.model;
import app.donation.model.Donation;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private User owner;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDateTime cardExpiration;

    @Column(nullable = false, unique = true)
    private String cardCVV;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "creditCard")
    private List<Donation> donations;
}
