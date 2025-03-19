package app.web;

import app.adoption.model.Adoption;
import app.adoption.service.AdoptionService;
import app.pet.model.Pet;
import app.pet.service.PetService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {

    private final UserService userService;
    private final PetService petService;
    private final AdoptionService adoptionService;

    public IndexController(UserService userService, PetService petService, AdoptionService adoptionService) {
        this.userService = userService;
        this.petService = petService;
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");

        modelAndView.addObject("registerRequest", RegisterRequest.builder().build());
        return modelAndView;
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        this.userService.register(registerRequest);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) String errorParam) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", LoginRequest.builder().build());

        if (errorParam != null) {
            modelAndView.addObject("errorMessage", "Incorrect username or password.");
        }
        return modelAndView;
    }

    @GetMapping("/pets-care")
    public ModelAndView getPetsCarePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pets-care");

        return modelAndView;
    }

    @GetMapping("/cat-care")
    public ModelAndView getCatsCarePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cat-care");

        return modelAndView;
    }

    @GetMapping("/dog-care")
    public ModelAndView getDogsCarePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dog-care");

        return modelAndView;
    }
}
