package ru.javarush.todo.validation.dto;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface IsPasswordValid {

    String message() default "The password doesn't comply the rules";

    int minLength() default 5;

    int maxLength() default 20;

    String pattern() default "\\S+";

    boolean shouldMatchThePattern() default false;

    boolean isNullable() default false;

}
