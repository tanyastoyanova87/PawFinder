package app.web;
import app.exception.DuplicateAdoptionRequestException;
import app.exception.InsufficientFundsException;
import app.exception.ResourceNotFoundException;
import app.exception.UsernameAlreadyExistException;
import app.security.AuthenticationMetaData;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final UserService userService;

    public GlobalExceptionHandler(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public void addUserGlobally(@AuthenticationPrincipal AuthenticationMetaData authenticationMetaData, Model model) {
        if (authenticationMetaData != null) {
            User user = userService.getById(authenticationMetaData.getId());
            model.addAttribute("user", user);
        }
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public String handleUsernameAlreadyExist(RedirectAttributes redirectAttributes, UsernameAlreadyExistException exception) {
        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("usernameAlreadyExistException", message);
        return "redirect:" + exception.getUrl();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class,
            HttpRequestMethodNotSupportedException.class,
            ResourceNotFoundException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("not-found");
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateAdoptionRequestException.class)
    public ModelAndView handleDuplicateAdoption() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("duplicate-adoption");
        return modelAndView;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public String handleInsufficientFunds(RedirectAttributes redirectAttributes, InsufficientFundsException exception) {
        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("insufficientFundsException", message);
        return "redirect:/donations/donation";
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("internal-server-error");
        return modelAndView;
    }
}
