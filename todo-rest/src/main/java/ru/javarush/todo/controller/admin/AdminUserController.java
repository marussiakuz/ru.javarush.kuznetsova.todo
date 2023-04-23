package ru.javarush.todo.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.facade.admin.AdminUserServiceFacade;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserServiceFacade adminUserServiceFacade;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return adminUserServiceFacade.getAll();
    }

    @PostMapping
    public UserResponseDto create(@RequestBody UserRequestDto user) {
        return adminUserServiceFacade.create(user);
    }

    @PatchMapping("/user/{id}")
    public UserResponseDto update(@PathVariable long id, @RequestBody UserRequestDto user) {
        return adminUserServiceFacade.update(id, user);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") long id) {
        adminUserServiceFacade.delete(id);
    }
}
