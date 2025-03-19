package app.web;

import app.adoption.model.Adoption;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getUserProfile(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-profile");

        User user = this.userService.getById(id);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{id}/profile/adoption-requests")
    public ModelAndView getUserAdoptionRequests(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adoption-requests");

        User user = this.userService.getById(id);
        List<Adoption> adoptions = userService.sortAdoptionRequests(user);

        modelAndView.addObject("user", user);
        modelAndView.addObject("adoptions", adoptions);

        return modelAndView;
    }

    @GetMapping("/{id}/profile/completed-adoptions")
    public ModelAndView getUserAdoptions(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("completed-adoptions");

        User user = this.userService.getById(id);
        List<Adoption> adoptions = userService.sortAdoptionRequests(user);

        modelAndView.addObject("user", user);
        modelAndView.addObject("adoptions", adoptions);

        return modelAndView;
    }


}
