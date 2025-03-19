package app.web;

import app.security.AuthenticationMetaData;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute("user")
    public void addUserGlobally(@AuthenticationPrincipal AuthenticationMetaData authenticationMetaData, Model model) {
        model.addAttribute("user", authenticationMetaData);
    }
}
