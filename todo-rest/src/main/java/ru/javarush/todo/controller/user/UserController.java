package ru.javarush.todo.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.facade.user.UserServiceFacade;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceFacade userServiceFacade;

    @GetMapping
    public UserResponseDto getUser() {
        return userServiceFacade.getUser();
    }
}
