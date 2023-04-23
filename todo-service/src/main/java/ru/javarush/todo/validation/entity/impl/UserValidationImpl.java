package ru.javarush.todo.validation.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.javarush.todo.exception.UserNotFoundException;
import ru.javarush.todo.repository.UserRepository;
import ru.javarush.todo.validation.entity.UserValidation;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidationImpl implements UserValidation {

    private final UserRepository userRepository;

    @Override
    public void verifyUserExistenceById(Long id) {
        log.debug("Start validation does the user with id = {} exist", id);

        if (!userRepository.existsById(id)) {
            log.error("User with id = {} doesn't exist", id);
            throw new UserNotFoundException("User with id = %s doesn't exist".formatted(id));
        }

        log.info("Success validation existence of the user with id = {}", id);
    }
}
