package app.adoption.model;

import app.pet.model.*;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Adoption {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private boolean ownOtherPets;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDateTime requestedOn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Pet pet;
}
