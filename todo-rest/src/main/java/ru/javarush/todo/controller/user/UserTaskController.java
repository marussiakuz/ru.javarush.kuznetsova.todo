package ru.javarush.todo.controller.user;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javarush.todo.dto.OnCreate;
import ru.javarush.todo.dto.OnUpdate;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;
import ru.javarush.todo.facade.user.UserTaskServiceFacade;

@RequiredArgsConstructor
@RequestMapping("/api/user/tasks")
@RestController
public class UserTaskController {

    private final UserTaskServiceFacade userTaskServiceFacade;

    @GetMapping
    public Page<TaskResponseDto> getTasks(@AuthenticationPrincipal(errorOnInvalidType = true, expression = "id")
                                              final Long userId,
                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return userTaskServiceFacade.getTasks(userId, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public TaskFullResponseDto getTaskById(@AuthenticationPrincipal(expression = "id") final Long userId,
                                           @PathVariable("id") Long taskId) {
        return userTaskServiceFacade.getTask(userId, taskId);
    }

    @PostMapping
    public TaskResponseDto createTask(@AuthenticationPrincipal(expression = "id") final Long userId,
                                      @RequestBody @Validated({OnCreate.class, Default.class}) TaskRequestDto taskRequestDto) {
        return userTaskServiceFacade.createTask(userId, taskRequestDto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDto updateTask(@AuthenticationPrincipal(expression = "id") final Long userId,
                                      @PathVariable("id") Long taskId,
                                      @RequestBody @Validated({OnUpdate.class, Default.class}) TaskRequestDto taskRequestDto) {
        return userTaskServiceFacade.updateTask(userId, taskId, taskRequestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@AuthenticationPrincipal(expression = "id") final Long userId, @PathVariable("id") Long taskId) {
        userTaskServiceFacade.deleteTask(userId, taskId);
    }
}
