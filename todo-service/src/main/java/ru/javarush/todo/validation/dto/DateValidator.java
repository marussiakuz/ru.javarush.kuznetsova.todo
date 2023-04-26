package ru.javarush.todo.validation.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<IsFuture, LocalDate> {

    private boolean isNullable;

    @Override
    public void initialize(IsFuture constraintAnnotation) {
        isNullable = constraintAnnotation.isNullable();
    }

    @Override
    public boolean isValid(LocalDate inputDate, ConstraintValidatorContext constraintValidatorContext) {
        if (isNullable && inputDate == null) return true;
        return !inputDate.isBefore(LocalDate.now());
    }
}
