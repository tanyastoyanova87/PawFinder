package app.web.dto;

import app.user.model.Country;
import app.validation.PasswordMatcher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@PasswordMatcher
public class RegisterRequest {

    @NotBlank(message = "First name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]+", message = "First name must start with a capital letter.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Last name must start with a capital letter.")
    private String lastName;

    @NotBlank(message = "Username cannot be empty.")
    @Length(min = 6, max = 10, message = "Username must be between 6 and 10 characters.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,10}$", message = "Password must be between 6 and 10 digits and letters, including capitalized letter.")
    private String password;

    @NotBlank(message = "Confirm password cannot be empty.")
    private String confirmPassword;

    @NotNull(message = "Country must be selected.")
    private Country country;
}
