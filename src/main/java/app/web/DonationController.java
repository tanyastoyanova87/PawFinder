package app.web;
import app.donation.service.DonationService;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.DonationRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private final DonationService donationService;
    private final UserService userService;

    public DonationController(DonationService donationService, UserService userService) {
        this.donationService = donationService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getDonationPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("donation-page");

        modelAndView.addObject("donationRequest", DonationRequest.builder().build());
        return modelAndView;
    }

    @GetMapping("/ready-amount")
    public ModelAndView getReadyAmount(DonationRequest donationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("donation-page");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("donation-page");
        modelAndView.addObject("donationRequest", donationRequest);

        return modelAndView;
    }

    @PostMapping("/ready-amount")
    public ModelAndView setAmountToInput(@RequestParam(name = "amount") String amount, DonationRequest donationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("donation-page");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("donation-page");
        modelAndView.addObject("amount", amount);
        modelAndView.addObject("donationRequest", donationRequest);

        return modelAndView;
    }

    @PostMapping("/amount")
    public String getDonationAmount(@Valid @ModelAttribute DonationRequest donationRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetaData authenticationMetaData) {
        if (bindingResult.hasErrors()) {
            return "donation-page";
        }

        UUID userId = authenticationMetaData.getId();
        User user = userService.getById(userId);

        donationService.donateAmount(user, donationRequest);
        return "redirect:/users/" + userId + "/profile/payment";
    }

//
//    @PostMapping("/payment")
//    public ModelAndView payDonation()
}
