package ru.javarush.todo.controller.admin;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javarush.todo.dto.OnCreate;
import ru.javarush.todo.dto.OnUpdate;
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
    public UserResponseDto create(@RequestBody @Validated({OnCreate.class, Default.class}) UserRequestDto user) {
        return adminUserServiceFacade.create(user);
    }

    @PatchMapping("/user/{id}")
    public UserResponseDto update(@PathVariable("id") long id,
                                  @RequestBody @Validated({OnUpdate.class, Default.class}) UserRequestDto user) {
        return adminUserServiceFacade.update(id, user);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") long id) {
        adminUserServiceFacade.delete(id);
    }
}
