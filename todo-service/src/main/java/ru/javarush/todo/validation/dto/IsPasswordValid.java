package ru.javarush.todo.validation.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
@Repeatable(PasswordValidators.class)
public @interface IsPasswordValid {

    String message() default "The password doesn't comply the rules";

    int minLength() default 4;

    int maxLength() default 20;

    String pattern() default "\\S+";

    boolean shouldMatchThePattern() default true;

    boolean isNullable() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
