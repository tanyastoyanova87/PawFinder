package app.web;

import app.user.service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/about-us")
    public ModelAndView getContactUsPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("about-us");

        return modelAndView;
    }
}
