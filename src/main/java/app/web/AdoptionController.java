package app.web;

import app.adoption.model.Adoption;
import app.adoption.service.AdoptionService;
import app.pet.model.Pet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping("/admin-panel")
    public ModelAndView getAdminPanel() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-panel");

        List<Adoption> allAdoptions = adoptionService.getAllAdoptions();
        List<Adoption> pendingAdoptions = allAdoptions.stream().filter(adoption -> adoption.getRequestStatus().name().equals("PENDING")).toList();
        modelAndView.addObject("pendingAdoptions", pendingAdoptions);

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
