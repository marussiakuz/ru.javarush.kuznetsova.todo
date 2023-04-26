package ru.javarush.todo.facade.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.facade.admin.AdminUserServiceFacade;
import ru.javarush.todo.service.UserService;
import ru.javarush.todo.validation.entity.UserValidation;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AdminUserServiceFacadeImpl implements AdminUserServiceFacade {

    private final UserService userService;
    private final UserValidation userValidation;

    @Override
    public List<UserResponseDto> getAll() {
        return userService.getAll();
    }

    @Override
    public UserResponseDto create(UserRequestDto user) {
        return userService.create(user);
    }

    @Override
    public UserResponseDto update(long id, UserRequestDto user) {
        userValidation.verifyUserExistenceById(id);
        return userService.update(id, user);
    }

    @Override
    public void delete(long id) {
        userValidation.verifyUserExistenceById(id);
        userService.delete(id);
    }
}
