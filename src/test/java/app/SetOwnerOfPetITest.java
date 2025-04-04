package app;

import app.adoption.model.Adoption;
import app.adoption.model.RequestStatus;
import app.adoption.repository.AdoptionRepository;
import app.pet.model.*;
import app.pet.repository.PetRepository;
import app.pet.service.PetService;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SetOwnerOfPetITest {

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private UserService userService;

    @Test
    void givenAdoption_setOwnerOfPet() {
        User user = User.builder()
                .username("Someone")
                .firstName("Tosho")
                .lastName("Toshkov")
                .password("12345sksk")
                .confirmPassword("12345sksk")
                .email("tosho@abv.bg")
                .role(UserRole.USER)
                .country(Country.BULGARIA)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        Pet pet = Pet.builder()
                .name("Ssss")
                .owner(user)
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .isAdopted(false)
                .picture("www.hishihs.com")
                .description("Some description")
                .build();

        userRepository.save(user);
        petRepository.save(pet);

        Adoption adoption = Adoption.builder()
                .fullName("Tosho Toshkov")
                .age(20)
                .address("Neverland 12")
                .city("Sofia")
                .ownOtherPets(true)
                .ownYard(true)
                .ownHome(true)
                .requestedOn(LocalDateTime.now())
                .requestStatus(RequestStatus.PENDING)
                .owner(user)
                .pet(pet)
                .build();

        adoptionRepository.save(adoption);

        petService.getById(pet.getId());

        List<Pet> pets = new ArrayList<>();
        Pet pet1 = Pet.builder()
                .name("hhh")
                .owner(user)
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .isAdopted(false)
                .picture("www.jiji.com")
                .description("Some description")
                .build();

        Pet pet2 = Pet.builder()
                .name("iiii")
                .owner(user)
                .specie(Specie.CAT)
                .ageStatus(AgeStatus.ADULT)
                .hairLength(HairLength.SHORT)
                .gender(Gender.FEMALE)
                .vaccinated(true)
                .isAdopted(false)
                .picture("www.aaaa.com")
                .description("Some description")
                .build();

        pets.add(pet1);
        pets.add(pet2);
        user.setPets(pets);
        userService.setPetToUser(pet, user);
        petService.setOwnerOfPet(adoption);

        assertThat(pet.getOwner()).isEqualTo(user);
        assertThat(pet.isAdopted()).isEqualTo(true);
    }
}
