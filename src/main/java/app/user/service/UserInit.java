package app.user.service;

import app.user.model.Country;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {
    private final UserService userService;

    @Autowired
    public UserInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {

        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("Test")
                .username("admin")
                .password("admin123A")
                .confirmPassword("admin123A")
                .country(Country.BULGARIA)
                .build();

        userService.register(registerRequest);
        userService.changeRoleOfAdmin(registerRequest);
    }
}
