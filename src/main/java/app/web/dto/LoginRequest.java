package app.web.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
public class LoginRequest {

    @Length(min = 6, max = 10, message = "Username must be between 6 and 10 characters.")
    private String username;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,10}$", message = "Password must be between 6 and 10 digits and letters, including capitalized letter.")
    private String password;
}
