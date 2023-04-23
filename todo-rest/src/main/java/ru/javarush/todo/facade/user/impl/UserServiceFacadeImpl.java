package ru.javarush.todo.facade.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.facade.user.UserServiceFacade;
import ru.javarush.todo.service.UserService;

@RequiredArgsConstructor
@Component
public class UserServiceFacadeImpl implements UserServiceFacade {

    private final UserService userService;

    @Override
    public UserResponseDto getUser() {
        return userService.getByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }
}
