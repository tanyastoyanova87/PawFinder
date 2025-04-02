package app.web;

import app.adoption.model.Adoption;
import app.adoption.service.AdoptionService;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getRequests() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("requests");

        List<Adoption> pendingAdoptions = adoptionService.getAllPendingAdoptions();
        modelAndView.addObject("pendingAdoptions", pendingAdoptions);

        return modelAndView;
    }

    @PutMapping("/requests/{id}/approve")
    public String approveAdoption(@PathVariable UUID id) {
        Adoption adoption = adoptionService.getById(id);
        adoptionService.approveRequestStatus(adoption);

        return "redirect:/requests";
    }

    @PutMapping("/requests/{id}/reject")
    public String rejectAdoption(@PathVariable UUID id) {
        Adoption adoption = adoptionService.getById(id);
        adoptionService.rejectRequestStatus(adoption);

        return "redirect:/requests";
    }
}
