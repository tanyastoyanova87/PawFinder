package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Builder
@Getter
@Setter
public class EditProfileRequest {

    @Pattern(regexp = "[A-Z][a-z]+", message = "First name must start with a capital letter.")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+", message = "Last name must start with a capital letter.")
    private String lastName;

    @Email(message = "Email must be valid.")
    private String email;

    @URL(message = "Profile picture must be valid url address.")
    private String profilePicture;
}
