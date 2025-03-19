package app.adoption.service;

import app.adoption.model.Adoption;
import app.adoption.model.RequestStatus;
import app.adoption.repository.AdoptionRepository;
import app.exception.DomainException;
import app.pet.model.Pet;
import app.pet.service.PetService;
import app.user.model.User;
import app.web.dto.AdoptionRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetService petService;

    public AdoptionService(AdoptionRepository adoptionRepository, PetService petService) {
        this.adoptionRepository = adoptionRepository;
        this.petService = petService;
    }

    public void sendAdoptionRequest(Pet pet, User user, AdoptionRequest adoptionRequest) {
        if (adoptionRequest.getAge() < 18) {
            throw new DomainException("You must be 18 or older to adopt a pet!");
        }

        Adoption optionalAdoption = adoptionRepository
                .findAll()
                .stream()
                .filter(a -> a.getOwner().getId().equals(user.getId()) && a.getPet().getId().equals(pet.getId()))
                .findFirst()
                .orElse(null);
//        Optional<Adoption> optionalAdoption = adoptionRepository.findByPetId(pet.getId());

//        if (optionalAdoption.isPresent()) {
//            Adoption adoption = optionalAdoption.get();
//            User optionalUser = adoption.getOwners().stream().filter(a -> a.getId().equals(user.getId())).findFirst().orElse(null);
//            if (optionalUser != null) {
//                if (optionalUser.getId().equals(user.getId())) {
//                    throw new DomainException("You already sent adoption request for this pet.");
//                }
//            }
//        }

        if (optionalAdoption != null) {
            throw new DomainException("You already sent adoption request for this pet.");
        }

        Adoption adoption = Adoption.builder()
                .fullName(adoptionRequest.getFullName())
                .age(adoptionRequest.getAge())
                .ownOtherPets(adoptionRequest.isOwnOtherPets())
                .address(adoptionRequest.getAddress())
                .city(adoptionRequest.getCity())
                .requestedOn(LocalDateTime.now())
                .requestStatus(RequestStatus.PENDING)
                .owner(user)
                .pet(pet)
                .build();

        adoptionRepository.save(adoption);
    }

    public Adoption getById(UUID id) {
       return adoptionRepository.findById(id).orElseThrow(() -> new DomainException("Adoption with id [%s] does not exist.".formatted(id)));
    }

    public void approveRequestStatus(Adoption adoption, Pet pet) {
        petService.checkIfPetIsAdopted(adoptionRepository.findAllByPetId(pet.getId()), pet);

        adoption.setRequestStatus(RequestStatus.APPROVED);
        petService.setOwnerOfPet(adoption);

        adoptionRepository.save(adoption);
    }

    public void rejectRequestStatus(Adoption adoption) {
        adoption.setRequestStatus(RequestStatus.REJECTED);
        adoptionRepository.save(adoption);
    }

//    private List<User> addUserToAdoption(AdoptionRequest adoptionRequest, User user) {
//        List<User> owners = adoptionRequest.getPotentialOwners();
//        if (owners != null) {
//            owners.add(user);
//            return owners;
//        }
//
//        owners = new ArrayList<>();
//        owners.add(user);
//        return owners;
//    }
}
