package app.web;

import app.adoption.model.Adoption;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/{id}/profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUserProfile(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-profile");

        User user = this.userService.getById(id);
        modelAndView.addObject("user", user);

        return modelAndView;
    }


    @GetMapping("/edit")
    public ModelAndView getEditProfilePage(@PathVariable UUID id) {
        User user = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", DtoMapper.mapUserToEditProfileRequest(user));

        return modelAndView;
    }

    @PutMapping("/edit")
    public ModelAndView editUserProfile(@PathVariable UUID id, @Valid @ModelAttribute EditProfileRequest editProfileRequest, BindingResult bindingResult) {
        User user = userService.getById(id);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-profile");
            modelAndView.addObject("user");
            modelAndView.addObject("editProfileRequest", editProfileRequest);

            return modelAndView;
        }

        userService.editUserProfile(user, editProfileRequest);
        return new ModelAndView("redirect:/{id}/profile");
    }

    @GetMapping("/adoption-requests")
    public ModelAndView getUserAdoptionRequests(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adoption-requests");

        User user = this.userService.getById(id);
        List<Adoption> adoptions = userService.sortAdoptionRequests(user);
        List<Adoption> requests = adoptions.stream().filter(adoption -> adoption.getRequestStatus().name().equals("PENDING") || adoption.getRequestStatus().name().equals("REJECTED")).toList();

        modelAndView.addObject("user", user);
        modelAndView.addObject("requests", requests);

        return modelAndView;
    }

    @GetMapping("/completed-adoptions")
    public ModelAndView getUserAdoptions(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("completed-adoptions");

        User user = this.userService.getById(id);
        List<Adoption> adoptions = userService.sortAdoptionRequests(user);
        List<Adoption> approvedAdoptions = adoptions.stream().filter(adoption -> adoption.getRequestStatus().name().equals("APPROVED")).toList();
        modelAndView.addObject("user", user);
        modelAndView.addObject("approvedAdoptions", approvedAdoptions);

        return modelAndView;
    }


}
