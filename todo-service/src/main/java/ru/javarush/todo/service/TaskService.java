package ru.javarush.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;

public interface TaskService {

    Page<TaskResponseDto> getAllByUserId(long userId, Pageable pageable);

    TaskFullResponseDto getById(long taskId);

    TaskResponseDto create(long userId, TaskRequestDto taskRequestDto);

    TaskResponseDto updateById(long taskId, TaskRequestDto taskRequestDto);

    void deleteById(long taskId);

}
