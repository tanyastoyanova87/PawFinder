package app.web;

import app.adoption.service.AdoptionService;
import app.pet.model.Pet;
import app.pet.service.PetService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AdoptionRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

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
    public ModelAndView adoptionRequest(@PathVariable UUID id, @Valid @ModelAttribute AdoptionRequest adoptionRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData, RedirectAttributes redirectAttributes) {
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
