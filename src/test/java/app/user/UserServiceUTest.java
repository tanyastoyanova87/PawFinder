package app.user;

import app.adoption.model.Adoption;
import app.adoption.model.RequestStatus;
import app.creditCard.service.CreditCardService;
import app.email.client.dto.Email;
import app.email.service.EmailService;
import app.exception.ResourceNotFoundException;
import app.exception.UsernameAlreadyExistException;
import app.pet.model.Pet;
import app.security.AuthenticationMetaData;
import app.user.model.Country;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CreditCardService creditCardService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void whenGetAdmin_returnUserWithRoleAdmin() {
        User user = User.builder()
                .role(UserRole.ADMIN)
                .build();

        when(userRepository.findByRole(UserRole.ADMIN)).thenReturn(user);
        User admin = userService.getAdmin();

        assertThat(user.getRole()).isEqualTo(admin.getRole());
        verify(userRepository, times(1)).findByRole(UserRole.ADMIN);
    }

    @Test
    void givenMissingUser_whenGiveRoleToAdmin_thenExceptionIsThrown() {
        String username = "Username";

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.giveRoleToAdmin(registerRequest));
    }

    @Test
    void givenUserWithRoleUser_whenChangeRoleByAdmin_thenUserBecomesAdmin() {
        User user = User.builder()
                .role(UserRole.USER)
                .build();

        userService.changeRoleByAdmin(user);

        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithRoleAdmin_whenChangeRoleByAdmin_thenUserBecomesUser() {
        User user = User.builder()
                .role(UserRole.ADMIN)
                .build();

        userService.changeRoleByAdmin(user);

        assertThat(user.getRole()).isEqualTo(UserRole.USER);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserStatusActive_whenChangeStatus_thenUserInactive() {
        User user = User.builder()
                .isActive(true)
                .build();

        userService.changeStatus(user);

        assertThat(user.isActive()).isEqualTo(false);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenEditProfileRequestAndUser_whenEditUserProfile_thenEditUser() {
        EditProfileRequest editProfileRequest = EditProfileRequest.builder()
                .firstName("name")
                .lastName("name")
                .email("email")
                .profilePicture("www.sss.com")
                .build();

        User user = User.builder().build();
        userService.editUserProfile(user, editProfileRequest);

        assertThat(editProfileRequest.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(editProfileRequest.getLastName()).isEqualTo(user.getLastName());
        assertThat(editProfileRequest.getEmail()).isEqualTo(user.getEmail());
        assertThat(editProfileRequest.getProfilePicture()).isEqualTo(user.getProfilePicture());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenRegisterRequest_whenGiveRoleToAdmin_thenChangeRoleFromUserToAdmin() {
        User user = User.builder()
                .username("name")
                .role(UserRole.USER)
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("name")
                .build();

        when(userRepository.findByUsername("name")).thenReturn(Optional.of(user));
        userService.giveRoleToAdmin(registerRequest);

        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenRegisterRequest_whenInitializeUser_thenCreateNewUser() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("name")
                .username("username")
                .lastName("last")
                .email("email")
                .confirmPassword(passwordEncoder.encode("123123"))
                .password(passwordEncoder.encode("123123"))
                .country(Country.BULGARIA)
                .build();

        User user = userService.initializaUser(registerRequest);

        assertThat(registerRequest.getUsername()).isEqualTo(user.getUsername());
        assertThat(registerRequest.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(registerRequest.getLastName()).isEqualTo(user.getLastName());
        assertThat(registerRequest.getPassword()).isEqualTo(user.getPassword());
        assertThat(registerRequest.getConfirmPassword()).isEqualTo(user.getConfirmPassword());
        assertThat(registerRequest.getCountry()).isEqualTo(user.getCountry());
        assertThat(registerRequest.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void givenNonExistingUser_whenGetByUserId_thenThrowException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(userId));
    }

    @Test
    void givenExistingUser_whenGetByUserId_thenReturnUser() {
        UUID userId = UUID.randomUUID();

        User user = User.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User user1 = userService.getById(userId);
        assertThat(user.getId()).isEqualTo(user1.getId());
    }

    @Test
    void givenUser_whenSetPetToUser_thenAddPetToUserPets() {
        User user = User.builder().build();
        List<Pet> pets = new ArrayList<>();
        pets.add(Pet.builder().build());
        user.setPets(pets);
        Pet pet = Pet.builder().build();

        userService.setPetToUser(pet, user);

        assertThat(user.getPets().size()).isEqualTo(pets.size());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUser_whenGetApprovedAdoptions_thenReturnFilteredApprovedAdoptions() {
        User user = User.builder().build();
        List<Adoption> adoptions = new ArrayList<>();

        adoptions.add(Adoption.builder()
                .requestStatus(RequestStatus.APPROVED)
                .build());

        user.setAdoptions(adoptions);

        userService.getApprovedAdoptions(user);

        assertThat(user.getAdoptions().size()).isEqualTo(adoptions.size());
    }

    @Test
    void givenUser_whenGetPendingAndRejectedAdoptions_thenReturnFilteredPendingAndRejectedAdoptions() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .build();
        List<Adoption> adoptions = new ArrayList<>();

        adoptions.add(Adoption.builder()
                .requestStatus(RequestStatus.PENDING)
                .requestedOn(LocalDateTime.now())
                .build());
        adoptions.add(Adoption.builder()
                .requestStatus(RequestStatus.REJECTED)
                .requestedOn(LocalDateTime.now())
                .build());

        user.setAdoptions(adoptions);

        List<Adoption> adoptions1 = userService.getPendingAndRejectedAdoptions(user);

        assertThat(adoptions1).hasSize(2);
        assertThat(adoptions1).allMatch(adoption ->
                adoption.getRequestStatus() == RequestStatus.PENDING ||
                        adoption.getRequestStatus() == RequestStatus.REJECTED);
    }

    @Test
    void givenNonExistingUsername_whenLoadUserByUsername_thenThrowException() {
        String username = "Username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void givenExistingUsername_whenLoadUserByUsername_thenReturnUserDetails() {
        String username = "Username";

        User user = User.builder()
                .id(UUID.randomUUID())
                .password("123123")
                .role(UserRole.USER)
                .isActive(true)
                .profilePicture("www.jij.com")
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        AuthenticationMetaData authenticationMetaData = (AuthenticationMetaData) userService.loadUserByUsername(username);
        assertThat(user.getId()).isEqualTo(authenticationMetaData.getId());
        assertThat(user.getPassword()).isEqualTo(authenticationMetaData.getPassword());
        assertThat(user.getRole()).isEqualTo(authenticationMetaData.getRole());
        assertThat(user.isActive()).isEqualTo(authenticationMetaData.isActive());
        assertThat(user.getProfilePicture()).isEqualTo(authenticationMetaData.getProfilePicture());
        assertThat(user.getUsername()).isEqualTo(authenticationMetaData.getUsername());
    }

    @Test
    void givenUserIdAndStatus_whenGetEmailsByUser_thenReturnAllEmailsByUser() {
        UUID userId = UUID.randomUUID();
        String status = "SUCCESSFUL";

        List<Email> mockEmails = List.of(
                Email.builder().userId(userId).status("SUCCESSFUL").build(),
                Email.builder().userId(userId).status("FAILED").build());

        when(emailService.getAllEmails(userId)).thenReturn(mockEmails);
        List<Email> emailsByUser = userService.getEmailsByUser(userId, status);

        assertThat(emailsByUser).hasSize(1);
        assertThat(emailsByUser).allMatch(email -> email.getStatus().equals(status));
    }

    @Test
    void givenExistingUsername_whenRegisterUser_thenThrowException() {
        String username = "username";

        User user = User.builder()
                .username(username)
                .build();
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyExistException.class, () -> userService.register(registerRequest));
    }

    @Test
    void givenNonExistingUsername_whenRegisterUser_thenCreateAndSaveUser() {
        String username = "username";
        String email = "test@example.com";
        UUID userId = UUID.randomUUID();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(username)
                .email(email)
                .firstName("Name")
                .password("123456")
                .confirmPassword("123456")
                .country(Country.BULGARIA)
                .build();

        User user = User.builder()
                .id(userId)
                .username(username)
                .email(email)
                .firstName("Name")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User user1 = userService.initializaUser(registerRequest);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        userService.register(registerRequest);

        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getFirstName()).isEqualTo("Name");
    }

}
//    @Test
//    void givenAllUsers_whenSetUserEmails_thenSetSuccessfulAndFailedEmailsToEachUser() {
//        User user1 = User.builder().id(UUID.randomUUID()).build();
//        User user2 = User.builder().id(UUID.randomUUID()).build();
//        List<User> users = List.of(user1, user2);
//
//        List<Email> mockSuccessfulEmails = List.of(Email.builder().userId(user1.getId()).status("SUCCEEDED").build());
//        List<Email> mockFailedEmails = List.of(Email.builder().userId(user2.getId()).status("FAILED").build());
//
//        when(userService.getEmailsByUser(user1.getId(), "SUCCEEDED")).thenReturn(mockSuccessfulEmails);
//        when(userService.getEmailsByUser(user1.getId(), "FAILED")).thenReturn(Collections.emptyList());
//
//        when(userService.getEmailsByUser(user2.getId(), "SUCCEEDED")).thenReturn(Collections.emptyList());
//        when(userService.getEmailsByUser(user2.getId(), "FAILED")).thenReturn(mockFailedEmails);
//
//        userService.setUserEmails(users);
//
//        System.out.println("User1 Success Count: " + user1.getSuccessfulEmails());
//        System.out.println("User2 Failed Count: " + user2.getFailedEmails());
//
//        assertThat(user1.getSuccessfulEmails()).isEqualTo(1);
//        assertThat(user1.getFailedEmails()).isEqualTo(0);
//
//        assertThat(user2.getSuccessfulEmails()).isEqualTo(0);
//        assertThat(user2.getFailedEmails()).isEqualTo(1);
//
//        verify(userRepository, times(1)).save(user1);
//        verify(userRepository, times(1)).save(user2);
//    }

