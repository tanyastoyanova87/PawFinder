package app.pet.model;

import app.adoption.model.Adoption;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Specie specie;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeStatus ageStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HairLength hairLength;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private boolean isVaccinated;

    @Column(nullable = false)
    private boolean isAdopted;

    @Column(nullable = false)
    private String picture;

    @Column(columnDefinition = "longtext", nullable = false)
    private String description;

    @OneToMany(mappedBy = "pet")
    private List<Adoption> adoptions;
}
