package ru.javarush.todo.facade.user;

import org.springframework.data.domain.Page;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;

public interface UserTaskServiceFacade {

    Page<TaskResponseDto> getTasks(Long userId, Integer pageNumber, Integer pageSize);

    TaskFullResponseDto getTask(Long userId, Long taskId);

    TaskResponseDto createTask(Long userId, TaskRequestDto taskRequestDto);

    TaskResponseDto updateTask(Long userId, Long taskId, TaskRequestDto taskRequestDto);

    void deleteTask(Long userId, Long taskId);
}
