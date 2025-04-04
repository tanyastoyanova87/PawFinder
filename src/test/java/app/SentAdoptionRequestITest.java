package app;

import app.adoption.repository.AdoptionRepository;
import app.adoption.service.AdoptionService;
import app.exception.DuplicateAdoptionRequestException;
import app.pet.model.*;
import app.pet.repository.PetRepository;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.AdoptionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SentAdoptionRequestITest {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Test
    void givenPetUserAdoptionRequest_whenSendingAdoptionRequest_saveAndReturnAdoption() {
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

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .fullName("Tosho Toshkov")
                .age(20)
                .address("Neverland 12")
                .city("Sofia")
                .build();

        adoptionService.sendAdoptionRequest(pet, user, adoptionRequest);
        assertThat(adoptionRepository.findAll().size()).isEqualTo(1);
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void givenPetUserAdoptionRequest_whenSendingAdoptionRequestWithSamePetAndUser_ThrowException() {
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

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .fullName("Tosho Toshkov")
                .age(20)
                .address("Neverland 12")
                .city("Sofia")
                .build();

        adoptionService.sendAdoptionRequest(pet, user, adoptionRequest);
        assertThrows(DuplicateAdoptionRequestException.class, () -> adoptionService.sendAdoptionRequest(pet, user, adoptionRequest));
    }
}
