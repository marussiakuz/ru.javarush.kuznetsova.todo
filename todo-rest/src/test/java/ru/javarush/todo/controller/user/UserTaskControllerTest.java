package ru.javarush.todo.controller.user;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javarush.todo.dto.request.TaskRequestDto;
import ru.javarush.todo.dto.response.TaskFullResponseDto;
import ru.javarush.todo.dto.response.TaskResponseDto;
import ru.javarush.todo.error.ErrorHandler;
import ru.javarush.todo.exception.TaskNotFoundException;
import ru.javarush.todo.facade.user.UserTaskServiceFacade;
import ru.javarush.todo.model.Status;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTaskControllerTest {

    @Mock
    private UserTaskServiceFacade userTaskServiceFacade;
    @InjectMocks
    private UserTaskController userTaskController;
    @InjectMocks
    private ObjectMapper mapper;
    private MockMvc mockMvc;
    private final static DateTimeFormatter DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final static DateTimeFormatter DATE_TIME_FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userTaskController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenGetTasksThenReturnPageWithTasksAndStatusIsOk() throws Exception {
        Mockito
                .when(userTaskServiceFacade.getTasks(null, 0, 10))
                .thenReturn(createTasks());

        mockMvc.perform(get("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", Is.is(false)))
                .andExpect(jsonPath("$.totalElements", Is.is(4)))
                .andExpect(jsonPath("$.size", Is.is(4)))
                .andExpect(jsonPath("$.sort.unsorted", Is.is(true)))

                .andExpect(jsonPath("$.content.length()", Is.is(4)))

                .andExpect(jsonPath("$.content[0].id", Is.is(1)))
                .andExpect(jsonPath("$.content[0].title", Is.is("Title 1")))
                .andExpect(jsonPath("$.content[0].deadline", Is.is(LocalDate.now().plusDays(1)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.content[0].status", Is.is("NEW")))

                .andExpect(jsonPath("$.content[1].id", Is.is(2)))
                .andExpect(jsonPath("$.content[1].title", Is.is("Title 2")))
                .andExpect(jsonPath("$.content[1].deadline", Is.is(LocalDate.now().plusDays(2)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.content[1].status", Is.is("NEW")))

                .andExpect(jsonPath("$.content[2].id", Is.is(3)))
                .andExpect(jsonPath("$.content[2].title", Is.is("Title 3")))
                .andExpect(jsonPath("$.content[2].deadline", Is.is(LocalDate.now().plusDays(3)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.content[2].status", Is.is("NEW")))

                .andExpect(jsonPath("$.content[3].id", Is.is(4)))
                .andExpect(jsonPath("$.content[3].title", Is.is("Title 4")))
                .andExpect(jsonPath("$.content[3].deadline", Is.is(LocalDate.now().plusDays(4)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.content[3].status", Is.is("NEW")));
    }

    @Test
    void givenNoTasksWhenGetTasksThenReturnEmptyPageAndStatusIsOk() throws Exception {
        Mockito
                .when(userTaskServiceFacade.getTasks(null, 0, 10))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        mockMvc.perform(get("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty", Is.is(true)))
                .andExpect(jsonPath("$.totalElements", Is.is(0)))
                .andExpect(jsonPath("$.size", Is.is(0)))
                .andExpect(jsonPath("$.sort.unsorted", Is.is(true)))

                .andExpect(jsonPath("$.content.length()", Is.is(0)));
    }

    @Test
    void whenGetTaskByIdThenReturnTaskFullResponseDtoAndStatusIsOk() throws Exception {
        TaskFullResponseDto taskOutput = TaskFullResponseDto.builder()
                .id(11)
                .title("Some title")
                .description("Some description")
                .deadline(LocalDate.now().plusDays(4))
                .status("IN_PROGRESS")
                .build();

        Mockito
                .when(userTaskServiceFacade.getTask(null, 11L))
                .thenReturn(taskOutput);

        mockMvc.perform(get("/api/user/tasks/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(11)))
                .andExpect(jsonPath("$.title", Is.is("Some title")))
                .andExpect(jsonPath("$.description", Is.is("Some description")))
                .andExpect(jsonPath("$.deadline", Is.is(LocalDate.now().plusDays(4)
                        .format(DATE_TIME_FORMATTER_FULL))))
                .andExpect(jsonPath("$.status", Is.is("IN_PROGRESS")));
    }

    @Test
    void givenIdOfNotExistsTaskWhenGetTaskByIdThenThrowsTaskNotFoundExceptionAndStatusIsNotFound() throws Exception {
        Mockito
                .when(userTaskServiceFacade.getTask(null, 11L))
                .thenThrow(new TaskNotFoundException("User with id = 0 doesn't have such a task with id = 11"));

        mockMvc.perform(get("/api/user/tasks/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(jsonPath("$.reason", Is.is("Task doesn't exist")))
                .andExpect(jsonPath("$.message", Is.is("User with id = 0 doesn't have such a task with id = 11")));

    }

    @Test
    void givenIncorrectIdWhenGetTaskByIdThenThrowsTaskNotFoundExceptionAndStatusIsBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/tasks/hghgkjg")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the url parameter")))
                .andExpect(jsonPath("$.message", Is.is("Incorrect url path variable - \"hghgkjg\"")));

    }

    @Test
    void whenCreateTaskThenReturnTaskResponseDtoAndStatusIsOk() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("Some title")
                .description("Some description")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        TaskResponseDto taskOutput = TaskResponseDto.builder()
                .id(113)
                .title("Some title")
                .deadline(LocalDate.now().plusDays(4).format(DATE_TIME_FORMATTER_SHORT))
                .status("PAUSED")
                .build();

        Mockito
                .when(userTaskServiceFacade.createTask(null, taskInput))
                .thenReturn(taskOutput);

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(113)))
                .andExpect(jsonPath("$.title", Is.is("Some title")))
                .andExpect(jsonPath("$.deadline", Is.is(LocalDate.now().plusDays(4)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.status", Is.is("PAUSED")));
    }

    @Test
    void givenTaskWithBlankTitleWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("")
                .description("Some description")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Title must be specify and it shouldn't be blank")));
    }

    @Test
    void givenTaskWithoutTitleWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .description("Some description")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Title must be specify and it shouldn't be blank")));
    }

    @Test
    void givenTaskWithBlankDescriptionWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .description("     ")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Description must be specify and it shouldn't be blank")));
    }

    @Test
    void givenTaskWithoutDescriptionWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Description must be specify and it shouldn't be blank")));
    }

    @Test
    void givenTaskWithPastDeadlineWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .description("some description")
                .deadline(LocalDate.now().minusDays(15))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Deadline must be in the future")));
    }

    @Test
    void givenTaskWithoutDeadlineWhenCreateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .description("some description")
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(post("/api/user/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Deadline must be specify")));
    }

    @Test
    void whenUpdateTaskThenReturnTaskResponseDtoAndStatusIsOk() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("Another title")
                .status(Status.PAUSED)
                .build();

        TaskResponseDto taskOutput = TaskResponseDto.builder()
                .id(113)
                .title("Another title")
                .deadline(LocalDate.now().plusDays(4).format(DATE_TIME_FORMATTER_SHORT))
                .status("PAUSED")
                .build();

        Mockito
                .when(userTaskServiceFacade.updateTask(null, 113L, taskInput))
                .thenReturn(taskOutput);

        mockMvc.perform(patch("/api/user/tasks/113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(113)))
                .andExpect(jsonPath("$.title", Is.is("Another title")))
                .andExpect(jsonPath("$.deadline", Is.is(LocalDate.now().plusDays(4)
                        .format(DATE_TIME_FORMATTER_SHORT))))
                .andExpect(jsonPath("$.status", Is.is("PAUSED")));
    }

    @Test
    void givenTaskWithBlankTitleWhenUpdateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("")
                .description("Some description")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(patch("/api/user/tasks/113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Title must not be blank")));
    }

    @Test
    void givenTaskWithBlankDescriptionWhenUpdateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .description("     ")
                .deadline(LocalDate.now().plusDays(4))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(patch("/api/user/tasks/113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Description must not be blank")));
    }

    @Test
    void givenTaskWithPastDeadlineWhenUpdateTaskThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("some title")
                .description("some description")
                .deadline(LocalDate.now().minusDays(15))
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(patch("/api/user/tasks/113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Deadline must be in the future")));
    }

    @Test
    void givenIncorrectTaskIdWhenUpdateTaskThenReturnTaskResponseDtoAndStatusIsOk() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("Another title")
                .status(Status.PAUSED)
                .build();

        mockMvc.perform(patch("/api/user/tasks/hh113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the url parameter")))
                .andExpect(jsonPath("$.message", Is.is("Incorrect url path variable - \"hh113\"")));
    }

    @Test
    void givenTaskNotExistsWhenUpdateTaskThenThrowsTaskNotFoundExceptionAndStatusIsNotFound() throws Exception {
        TaskRequestDto taskInput = TaskRequestDto.builder()
                .title("Another title")
                .status(Status.PAUSED)
                .build();

        Mockito
                .when(userTaskServiceFacade.updateTask(null, 113L, taskInput))
                .thenThrow(new TaskNotFoundException("User with id = 0 doesn't have such a task with id = 113"));

        mockMvc.perform(patch("/api/user/tasks/113")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskInput)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(jsonPath("$.reason", Is.is("Task doesn't exist")))
                .andExpect(jsonPath("$.message", Is.is("User with id = 0 doesn't have such a task with id = 113")));
    }

    @Test
    void whenSuccessfullyDeleteTaskThenStatusIsOk() throws Exception {
        Mockito
                .doNothing()
                .when(userTaskServiceFacade).deleteTask(null, 12L);

        mockMvc.perform(MockMvcRequestBuilders.delete(URI.create("/api/user/tasks/12")))
                .andExpect(status().isOk());
    }

    @Test
    void givenNotExistsTaskIdWhenDeleteTaskThenThrowTaskNotFoundExceptionAndStatusIsNotFound() throws Exception {
        Mockito
                .doThrow(new TaskNotFoundException("User with id = 0 doesn't have such a task with id = 3"))
                .when(userTaskServiceFacade).deleteTask(null, 3L);

        mockMvc.perform(MockMvcRequestBuilders.delete(URI.create("/api/user/tasks/3")))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TaskNotFoundException))
                .andExpect(jsonPath("$.reason", Is.is("Task doesn't exist")))
                .andExpect(jsonPath("$.message", Is.is("User with id = 0 doesn't have such a task with id = 3")));
    }

    private Page<TaskResponseDto> createTasks() {
        return new PageImpl<>(IntStream.range(1, 5)
                .mapToObj(number -> TaskResponseDto.builder()
                        .id(number)
                        .title("Title %s".formatted(number))
                        .deadline(LocalDate.now().plusDays(number).format(DATE_TIME_FORMATTER_SHORT))
                        .status("NEW")
                        .build())
                .toList());
    }
}