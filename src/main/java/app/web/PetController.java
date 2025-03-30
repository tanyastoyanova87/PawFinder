package app.web;

import app.adoption.service.AdoptionService;
import app.pet.model.Pet;
import app.pet.service.PetService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddPetRequest;
import app.web.dto.AdoptionRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pets-for-adoption")
public class PetController {

    private final PetService petService;
    private final UserService userService;
    private final AdoptionService adoptionService;

    public PetController(PetService petService, UserService userService, AdoptionService adoptionService) {
        this.petService = petService;
        this.userService = userService;
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public ModelAndView getPetsForAdoptionPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pets-for-adoption");

        List<Pet> pets = petService.getAllPets().stream().filter(pet -> !pet.isAdopted()).toList();
        modelAndView.addObject("pets", pets);

        return modelAndView;
    }

    @GetMapping("/add-pet")
    public ModelAndView getNewPetForAdoptionPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-pet");
        modelAndView.addObject("addPetRequest", AddPetRequest.builder().build());

        return modelAndView;
    }

    @PostMapping("/add-pet")
    public String addNewPetForAdoption(@Valid @ModelAttribute AddPetRequest addPetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-pet";
        }

        petService.addPet(addPetRequest);
        return "redirect:/pets-for-adoption";
    }


//    @PostMapping("/{id}/like")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> likePet(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
//        Pet pet = petService.getById(id);
//        User user = userService.getById(authenticationMetaData.getId());
//        userService.likePet(pet, user);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("liked", user.getFavouritePets().contains(pet));
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{id}/like")
    public ModelAndView getFavouritePets(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
        ModelAndView modelAndView = new ModelAndView();
        List<Pet> allPets = petService.getAllPets();
        modelAndView.addObject("allPets", allPets);

        User user = userService.getById(authenticationMetaData.getId());
        Set<UUID> likedPets = allPets.stream()
                .filter(pet -> userService.isPetLikedByUser(pet, user))
                .map(Pet::getId).collect(Collectors.toSet());
        modelAndView.addObject("likedPets", likedPets);


        return new ModelAndView("redirect:/pets-for-adoption");
    }

    @GetMapping("/{id}/info")
    public ModelAndView getPetInfoPage(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pet-info");

        Pet pet = petService.getById(id);
        modelAndView.addObject("pet", pet);
        return modelAndView;
    }

    @GetMapping("/{id}/info/adoption")
    public ModelAndView getAdoptPetPage(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adoption");

        Pet pet = petService.getById(id);
        modelAndView.addObject("pet", pet);
        modelAndView.addObject("adoptionRequest", AdoptionRequest.builder().build());
        return modelAndView;
    }

    @PostMapping("/{id}/info/adoption")
    public ModelAndView adoptionRequest(@PathVariable UUID id, @Valid @ModelAttribute AdoptionRequest adoptionRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
        Pet pet = petService.getById(id);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("adoption");
            modelAndView.addObject("pet", pet);
            modelAndView.addObject("adoptionRequest", adoptionRequest);
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView();
        User user = userService.getById(authenticationMetaData.getId());

        adoptionService.sendAdoptionRequest(pet, user, adoptionRequest);

        modelAndView.addObject("pet", pet);
        modelAndView.addObject("adoptionRequest", adoptionRequest);

        return new ModelAndView("redirect:/pets-for-adoption");
    }
}
