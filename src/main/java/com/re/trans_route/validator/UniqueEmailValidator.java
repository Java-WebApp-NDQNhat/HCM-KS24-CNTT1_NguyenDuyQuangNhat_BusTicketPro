package com.re.trans_route.validator;

import com.re.trans_route.annotation.UniqueEmail;
import com.re.trans_route.repository.UserRepository;
import com.re.trans_route.service.UserService;
import jakarta.validation.ConstraintValidator;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean isValid(String email, jakarta.validation.ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
        }
        return !userService.isEmailExisted(email);
    }
}
