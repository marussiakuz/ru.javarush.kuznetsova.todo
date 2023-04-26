package ru.javarush.todo.converter.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.javarush.todo.config.MapperConfig;
import ru.javarush.todo.converter.DtoMapper;
import ru.javarush.todo.converter.EntityMapper;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskResponseDto;
import ru.javarush.todo.model.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapperConfig.class)
public interface TaskMapper extends DtoMapper<TaskResponseDto, Task>, EntityMapper<TaskRequestDto, Task> {

    @Mapping(target = "status", expression = "java(task.getStatus().name())")
    @Override
    TaskResponseDto toDto(Task task);

    default String map(LocalDate deadline) {
        return deadline.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
