package ru.javarush.todo.validation.entity.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.javarush.todo.exception.TaskNotFoundException;
import ru.javarush.todo.repository.TaskRepository;
import ru.javarush.todo.validation.entity.TaskValidation;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskValidationImpl implements TaskValidation {

    private final TaskRepository taskRepository;

    @Override
    public void verifyWhetherTaskBelongsToUser(long userId, long taskId) {
        log.debug("Start validation whether task with id = {} belongs to user with id = {}", userId, taskId);

        if (!taskRepository.existsByIdAndUserId(taskId, userId)) {
            log.error("User with id = {} doesn't have such a task with id = {}", userId, taskId);
            throw new TaskNotFoundException("User with id = %s doesn't have such a task with id = %s"
                    .formatted(userId, taskId));
        }

        log.info("Success validation whether user with id = {} has the task with id = {}", userId, taskId);
    }
}
