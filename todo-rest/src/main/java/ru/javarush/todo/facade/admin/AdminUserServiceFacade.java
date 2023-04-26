package ru.javarush.todo.facade.admin;

import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;

import java.util.List;

public interface AdminUserServiceFacade {

    List<UserResponseDto> getAll();

    UserResponseDto create(UserRequestDto user);

    UserResponseDto update(long id, UserRequestDto user);

    void delete(long id);
}
