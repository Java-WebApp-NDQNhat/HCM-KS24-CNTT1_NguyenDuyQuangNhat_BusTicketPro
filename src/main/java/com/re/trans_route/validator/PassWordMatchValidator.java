package com.re.trans_route.validator;

import com.re.trans_route.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PassWordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String password = (String) value.getClass().getMethod("getPassword").invoke(value);
            String rePass = (String) value.getClass().getMethod("getRePass").invoke(value);
            return password != null && password.equals(rePass);
        } catch (Exception e) {
            return false;
        }
    }
}
