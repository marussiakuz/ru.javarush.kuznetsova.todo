package ru.javarush.todo.converter.task;

import org.mapstruct.Mapper;
import ru.javarush.todo.config.MapperConfig;
import ru.javarush.todo.converter.DtoMapper;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.model.Task;

@Mapper(config = MapperConfig.class)
public interface TaskFullMapper extends DtoMapper<TaskFullResponseDto, Task> {
}
