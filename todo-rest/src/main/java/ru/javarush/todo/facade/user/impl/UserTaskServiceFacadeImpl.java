package ru.javarush.todo.facade.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;
import ru.javarush.todo.facade.user.UserTaskServiceFacade;
import ru.javarush.todo.service.TaskService;
import ru.javarush.todo.validation.entity.TaskValidation;

@RequiredArgsConstructor
@Component
public class UserTaskServiceFacadeImpl implements UserTaskServiceFacade {

    private final TaskService taskService;
    private final TaskValidation taskValidation;

    @Override
    public Page<TaskResponseDto> getTasks(Long userId, Integer pageNumber, Integer pageSize) {
        return taskService.getAllByUserId(userId, PageRequest.of(pageNumber, pageSize));
    }

    @Override
    public TaskFullResponseDto getTask(Long userId, Long taskId) {
        taskValidation.verifyWhetherTaskBelongsToUser(userId, taskId);
        return taskService.getById(taskId);
    }

    @Override
    public TaskResponseDto createTask(Long userId, TaskRequestDto taskRequestDto) {
        return taskService.create(userId, taskRequestDto);
    }

    @Override
    public TaskResponseDto updateTask(Long userId, Long taskId, TaskRequestDto taskRequestDto) {
        taskValidation.verifyWhetherTaskBelongsToUser(userId, taskId);
        return taskService.updateById(taskId, taskRequestDto);
    }

    @Override
    public void deleteTask(Long userId, Long taskId) {
        taskValidation.verifyWhetherTaskBelongsToUser(userId, taskId);
        taskService.deleteById(taskId);
    }
}
