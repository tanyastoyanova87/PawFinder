package app.web;

import app.adoption.model.Adoption;
import app.creditCard.model.CreditCard;
import app.email.client.dto.Email;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.EditProfileRequest;
import app.web.mapper.DtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUsersPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");

        List<User> allUsers = userService.getAllUsers();
        modelAndView.addObject("allUsers", allUsers);

        for (User user : allUsers) {
            List<Email> succeededEmails = userService.getEmailsByUser(user.getId(), "SUCCEEDED");
            List<Email> failedEmails = userService.getEmailsByUser(user.getId(), "FAILED");

            user.setSuccessfulEmails(succeededEmails.size());
            user.setFailedEmails(failedEmails.size());

            userService.setUserEmails(succeededEmails, failedEmails, user);
        }

        return modelAndView;
    }

    @PutMapping("/{id}/role")
    public String changeUserRole(@PathVariable UUID id) {
        User user = userService.getById(id);
        userService.changeRoleByAdmin(user);

        return "redirect:/users";
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getUserProfile(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-profile");

        User user = this.userService.getById(id);
        modelAndView.addObject("user", user);

        return modelAndView;
    }


    @GetMapping("/{id}/profile/edit")
    public ModelAndView getEditProfilePage(@PathVariable UUID id) {
        User user = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editProfileRequest", DtoMapper.mapUserToEditProfileRequest(user));

        return modelAndView;
    }

    @PutMapping("/{id}/profile/edit")
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
        return new ModelAndView("redirect:/users/{id}/profile");
    }

    @GetMapping("/{id}/profile/adoption-requests")
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

    @GetMapping("/{id}/profile/completed-adoptions")
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

    @GetMapping("/{id}/profile/payment")
    public ModelAndView getPaymentPage(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-credit-card");

        User user = userService.getById(id);
        CreditCard creditCard = user.getCreditCard();
        modelAndView.addObject("user", user);
        modelAndView.addObject("creditCard", creditCard);

        return modelAndView;
    }

    @PutMapping("/{id}/profile/delete")
    public String changeUserStatus(@PathVariable UUID id, HttpServletRequest request, HttpServletResponse response) {
        User user = this.userService.getById(id);

        userService.changeStatus(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }
}
