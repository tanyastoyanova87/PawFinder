package app.web.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdoptionRequest {

    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 letters.")
    @Pattern(regexp = "[A-Z][a-z]+ [A-Z][a-z]+", message = "Full name must start with capital letter and include first and last name.")
    @NotBlank(message = "Full name can not be empty.")
    private String fullName;

    @Min(value = 18, message = "Your age must be 18 or greater.")
    @NotNull(message = "Age can not be empty.")
    private int age;

    @Size(min = 3, max = 30, message = "Address name must be between 1 and 60 letters.")
    @Pattern(regexp = "^[A-Za-z]+(\\s[A-Za-z]+)*\\s\\d+$", message = "Address must include the number of the street.")
    @NotBlank(message = "Address can not be empty.")
    private String address;

    @Size(min = 1, max = 60, message = "City name must be between 1 and 60 letters.")
    @Pattern(regexp = "^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$", message = "City must start with capital letter.")
    @NotBlank(message = "City can not be empty.")
    private String city;

    private boolean ownOtherPets;
    private boolean ownHome;
    private boolean ownYard;
}
