package app.pet.service;

import app.adoption.model.Adoption;
import app.exception.DomainException;
import app.pet.model.Pet;
import app.pet.repository.PetRepository;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddPetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserService userService;

    @Autowired
    public PetService(PetRepository petRepository, UserService userService) {
        this.petRepository = petRepository;
        this.userService = userService;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public void addPet(AddPetRequest petRequest) {
        Optional<Pet> optionalPet = petRepository.findByName(petRequest.getName());
        if (optionalPet.isPresent()) {
            throw new DomainException("Pet with this name already exist.");
        }

        Pet pet = initializePet(petRequest);
        petRepository.save(pet);
    }

    private Pet initializePet(AddPetRequest petRequest) {
        return Pet.builder()
                .name(petRequest.getName())
                .specie(petRequest.getSpecie())
                .ageStatus(petRequest.getAgeStatus())
                .hairLength(petRequest.getHairLength())
                .gender(petRequest.getGender())
                .vaccinated(petRequest.isVaccinated())
                .isAdopted(false)
                .picture(petRequest.getPicture())
                .description(petRequest.getDescription())
                .build();
    }

    public Pet getById(UUID id) {
        return petRepository.findById(id).orElseThrow(() -> new DomainException("Pet with id [%s] does not exist.".formatted(id)));
    }

    public void setOwnerOfPet(Adoption adoption) {
        Pet pet = adoption.getPet();
        User owner = adoption.getOwner();
        pet.setOwner(owner);
        pet.setAdopted(true);

        userService.setPetToUser(pet, owner);

        petRepository.save(pet);
    }

    public void checkIfPetIsAdopted(List<Adoption> adoptions, Pet pet) {
        for (Adoption adoption : adoptions) {
            if (pet.isAdopted()) {
                throw new DomainException("Pet with id [%s] is already adopted.".formatted(adoption.getPet().getId()));
            }
        }
    }
}
