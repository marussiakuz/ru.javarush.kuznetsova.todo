package ru.javarush.todo.validation.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class PasswordValidator implements ConstraintValidator<IsPasswordValid, String> {

    private int minLength;
    private int maxLength;
    private String pattern;
    private boolean shouldMatchThePattern;
    private boolean isNullable;

    @Override
    public void initialize(IsPasswordValid constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
        pattern = constraintAnnotation.pattern();
        shouldMatchThePattern = constraintAnnotation.shouldMatchThePattern();
        isNullable = constraintAnnotation.isNullable();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(password)) return isNullable;

        return (!shouldMatchThePattern || Pattern.matches(pattern, password)) && pattern.length() >= minLength
                && pattern.length() <= maxLength;
    }
}
