package ru.javarush.todo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todo.converter.task.TaskFullMapper;
import ru.javarush.todo.converter.task.TaskMapper;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;
import ru.javarush.todo.exception.TaskNotFoundException;
import ru.javarush.todo.exception.UserNotFoundException;
import ru.javarush.todo.model.Task;
import ru.javarush.todo.model.User;
import ru.javarush.todo.repository.TaskRepository;
import ru.javarush.todo.repository.UserRepository;
import ru.javarush.todo.service.TaskService;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskFullMapper taskFullMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<TaskResponseDto> getAllByUserId(long userId, Pageable pageable) {
        return taskMapper.toDto(taskRepository.findAllByUserId(userId, pageable));
    }

    @Transactional(readOnly = true)
    @Override
    public TaskFullResponseDto getById(long taskId) {
        return taskFullMapper.toDto(taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id=%s doesn't exist".formatted(taskId))));
    }

    @Transactional
    @Override
    public TaskResponseDto create(long userId, TaskRequestDto taskRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id=%s doesn't exist".formatted(userId)));
        Task task = taskMapper.toEntity(taskRequestDto);
        task.setUser(user);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    @Override
    public TaskResponseDto updateById(long taskId, TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id = %s doesn't exist".formatted(taskId)));
        taskMapper.updateEntityFromDto(taskRequestDto, task);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Transactional
    @Override
    public void deleteById(long taskId) {
        taskRepository.deleteById(taskId);
    }
}
