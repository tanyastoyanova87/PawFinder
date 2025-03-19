package app.validation;

import app.web.dto.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatcherValidator implements ConstraintValidator<PasswordMatcher, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest user, ConstraintValidatorContext context) {
        if (user.getPassword() == null || user.getConfirmPassword() == null) {
            return false;
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Passwords do not match.")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;

        }
        return true;
    }
}
