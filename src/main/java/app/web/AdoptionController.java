package app.web;

import app.adoption.model.Adoption;
import app.adoption.service.AdoptionService;
import app.pet.model.Pet;
import app.pet.service.PetService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class AdoptionController {

    private final UserService userService;
    private final AdoptionService adoptionService;

    public AdoptionController(UserService userService, AdoptionService adoptionService) {
        this.userService = userService;
        this.adoptionService = adoptionService;
    }

    @GetMapping("/admin-panel")
    public ModelAndView getAdminPanel(@AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-panel");

        User user = userService.getById(authenticationMetaData.getId());
        List<User> allUsers = userService.getAllUsers();
        modelAndView.addObject("user", user);
        modelAndView.addObject("allUsers", allUsers);

        return modelAndView;
    }

    @PutMapping("/admin-panel/{id}/approve")
    public String approveAdoption(@PathVariable UUID id) {
        Adoption adoption = adoptionService.getById(id);
        Pet pet = adoption.getPet();
        adoptionService.approveRequestStatus(adoption, pet);

        return "redirect:/admin-panel";
    }

    @PutMapping("/admin-panel/{id}/reject")
    public String rejectAdoption(@PathVariable UUID id) {
        Adoption adoption = adoptionService.getById(id);
        adoptionService.rejectRequestStatus(adoption);

        return "redirect:/admin-panel";
    }
}
