package ru.javarush.todo.service;

import java.util.List;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.dto.request.UserRequestDto;

public interface UserService {

    List<UserResponseDto> getAll();

    UserResponseDto create(UserRequestDto user);

    UserResponseDto update(long id, UserRequestDto user);

    UserResponseDto getByEmail(String email);

    void delete(long id);
}
