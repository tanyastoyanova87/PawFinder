package app.web.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdoptionRequest {

    @Pattern(regexp = "^(?=.{3,50}$)[A-Z][a-z]+ [A-Z][a-z]+$", message = "Full name must start with capital letter, including first and last name and must be between 3 and 50 characters.")
    @NotBlank(message = "Full name can not be empty.")
    private String fullName;

    @Min(value = 18, message = "Your age must be 18 or greater.")
    @NotNull(message = "Age can not be empty.")
    private int age;

    @Pattern(regexp = "^(?=.{3,30}$)[A-Za-z]+(\\s[A-Za-z]+)*\\s\\d+$", message = "Address must include the number of the street and must be between 3 and 30 characters.")
    @NotBlank(message = "Address can not be empty.")
    private String address;

    @Pattern(regexp = "^(?=.{1,60}$)[A-Z][a-z]+(\\s[A-Z][a-z]+)*$", message = "City must start with capital letter and must be between 1 and 60 characters.")
    @NotBlank(message = "City can not be empty.")
    private String city;

    private boolean ownOtherPets;
    private boolean ownHome;
    private boolean ownYard;
}
