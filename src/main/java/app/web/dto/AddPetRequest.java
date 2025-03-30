package app.web.dto;

import app.pet.model.AgeStatus;
import app.pet.model.Gender;
import app.pet.model.HairLength;
import app.pet.model.Specie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Builder
@Getter
@Setter
public class AddPetRequest {

    @NotBlank(message = "Name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Name must start with a capital letter.")
    private String name;

    @NotNull(message = "Specie must be selected.")
    private Specie specie;

    @NotNull(message = "Age status must be selected.")
    private AgeStatus ageStatus;

    @NotNull(message = "Hair length must be selected.")
    private HairLength hairLength;

    @NotNull(message = "Gender must be selected.")
    private Gender gender;

    private boolean vaccinated;

    @URL(message = "Must be valid url address.")
    @NotBlank(message = "Picture cannot be empty.")
    private String picture;

    @Size(min = 10, max = 2000, message = "Description must be between 5 and 2000 letters.")
    @NotBlank(message = "Description cannot be empty.")
    private String description;
}
