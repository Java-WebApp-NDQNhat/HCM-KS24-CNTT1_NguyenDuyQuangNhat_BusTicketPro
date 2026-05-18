package com.re.trans_route.annotation;

import com.re.trans_route.validator.PassWordMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PassWordMatchValidator.class)
@Target(ElementType.TYPE)
public @interface PasswordMatch {
    String message() default "Mật khẩu xác nhận không khớp với mật khẩu";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
