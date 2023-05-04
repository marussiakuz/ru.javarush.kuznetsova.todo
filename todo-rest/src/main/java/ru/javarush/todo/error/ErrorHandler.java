package ru.javarush.todo.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.javarush.todo.exception.TaskNotFoundException;
import ru.javarush.todo.exception.UserNotFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .reason(e instanceof UserNotFoundException ? "User doesn't exist" : "Task doesn't exist")
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentTypeMismatchException e) {
        String[] messageParts = e.getMessage().split(" ");

        return ErrorResponse.builder()
                .reason("Error in the url parameter")
                .message("Incorrect url path variable - %s".formatted(messageParts[messageParts.length - 1]))
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ErrorResponse.builder()
                .reason("Error in the object field")
                .message(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
