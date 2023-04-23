package ru.javarush.todo.validation.dto;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface IsFuture {

    String message() default "The date must be no later than the current date";

    boolean isNullable() default false;
}
