package com.re.trans_route.annotation;

import com.re.trans_route.validator.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email đã được đăng ký!!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
