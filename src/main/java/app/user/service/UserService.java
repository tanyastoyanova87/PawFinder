package app.user.service;

import app.adoption.model.Adoption;
import app.exception.DomainException;
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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    private User login(LoginRequest loginRequest) {
//        Optional<User> userOptional = this.userRepository.findByUsername(loginRequest.getUsername());
//        if (userOptional.isEmpty()) {
//            throw new DomainException("Incorrect username or password.");
//        }
//
//        User user = userOptional.get();
//        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new DomainException("Incorrect username or password.");
//        }
//
//        log.info("User [%s] logged in.".formatted(user.getUsername()));
//        return user;
//    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> userOptional = this.userRepository.findByUsername(registerRequest.getUsername());
        if (userOptional.isPresent()) {
            throw new DomainException("Username [%s] already exist.".formatted(registerRequest.getUsername()));
        }

        User user = initializaUser(registerRequest);

        this.userRepository.save(user);
        log.info("Successfully created user with username [%s].".formatted(user.getUsername()));
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
                .build();
    }

    public User getById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist."));

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
        return userRepository.findByUsername(registerRequest.getUsername()).orElseThrow(() -> new DomainException("User with username [%s] does not exist.".formatted(registerRequest.getUsername())));
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
}
