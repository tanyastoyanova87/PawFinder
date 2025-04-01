package app.user.service;

import app.adoption.model.Adoption;
import app.creditCard.model.CreditCard;
import app.creditCard.service.CreditCardService;
import app.email.client.dto.Email;
import app.email.service.EmailService;
import app.exception.ResourceNotFoundException;
import app.exception.UsernameAlreadyExistException;
import app.pet.model.Pet;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.EditProfileRequest;
import app.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreditCardService creditCardService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CreditCardService creditCardService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.creditCardService = creditCardService;
        this.emailService = emailService;
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> userOptional = this.userRepository.findByUsername(registerRequest.getUsername());

        if (userOptional.isPresent()) {
            throw new UsernameAlreadyExistException("Username %s already exist.".formatted(registerRequest.getUsername()), "/register");
        }

        User user = initializaUser(registerRequest);
        userRepository.save(user);

        CreditCard creditCard = creditCardService.generateCreditCard(user);
        user.setCreditCard(creditCard);
        userRepository.save(user);

        log.info("Successfully created user with username [%s].".formatted(user.getUsername()));

        String subjectEmail = "Successful registration";
        String bodyEmail = "Welcome to PawFinder, %s!%nWe're excited to have you as part of our community. A virtual credit card has been created for you. You can use it to support our pets in need.%nBest regards!%nThe PawFinder Team".formatted(user.getFirstName());
        String email = user.getEmail();
        String sender = "pawfinder2025@gmail.com";
        emailService.sendEmail(user.getId(), subjectEmail, bodyEmail, email, sender);
    }

    public User initializaUser(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getUsername())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .confirmPassword(passwordEncoder.encode(registerRequest.getConfirmPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .profilePicture("https://images.icon-icons.com/3298/PNG/512/ui_user_profile_avatar_person_icon_208734.png")
                .country(registerRequest.getCountry())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .successfulEmails(0)
                .failedEmails(0)
                .build();
    }

    public User getById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id [%s] does not exist.".formatted(id)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with this username does not exist."));

        return new AuthenticationMetaData(user.getId(), user.getUsername(), user.getPassword(),
                user.getRole(), user.isActive(), user.getProfilePicture(), user.getFavouritePets());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void changeRoleOfAdmin(RegisterRequest registerRequest) {
        User user = getUserByUsername(registerRequest);
        user.setRole(UserRole.ADMIN);

        userRepository.save(user);
    }

    private User getUserByUsername(RegisterRequest registerRequest) {
        return userRepository.findByUsername(registerRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User with username [%s] does not exist.".formatted(registerRequest.getUsername())));
    }

    public List<Adoption> sortAdoptionRequests(User user) {
        return user.getAdoptions().stream().sorted(Comparator.comparing(Adoption::getRequestedOn)).toList();
    }

    public void setPetToUser(Pet pet, User owner) {
        List<Pet> pets = owner.getPets();
        pets.add(pet);

        userRepository.save(owner);
    }

    public void likePet(Pet pet, User user) {
        List<Pet> favouritePets = user.getFavouritePets();
        if (favouritePets.contains(pet)) {
            favouritePets.remove(pet);
        } else {
            favouritePets.add(pet);
        }

        userRepository.save(user);
    }

    public boolean isPetLikedByUser(Pet pet, User user) {
        return user.getFavouritePets().stream()
                .anyMatch(p -> p.getId().equals(pet.getId()));
    }

    public void editUserProfile(User user, EditProfileRequest editProfileRequest) {
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setEmail(editProfileRequest.getEmail());
        user.setProfilePicture(editProfileRequest.getProfilePicture());

        userRepository.save(user);
    }

    public List<Email> getEmailsByUser(UUID userId, String status) {
        List<Email> allEmails = emailService.getAllEmails(userId);

        return allEmails.stream()
                .filter(email -> email.getStatus()
                        .equals(status) && email.getUserId().equals(userId))
                .toList();
    }

    public void changeRoleByAdmin(User user) {
        if (user.getRole().name().equals("USER")) {
            user.setRole(UserRole.ADMIN);
        } else {
            user.setRole(UserRole.USER);
        }

        userRepository.save(user);
    }

    public void setUserEmails(List<Email> succeededEmails, List<Email> failedEmails, User user) {
        user.setSuccessfulEmails(succeededEmails.size());
        user.setFailedEmails(failedEmails.size());

        userRepository.save(user);
    }

    public void changeStatus(User user) {
        if (user.isActive()) {
            user.setActive(false);
        }

        userRepository.save(user);
    }
}
