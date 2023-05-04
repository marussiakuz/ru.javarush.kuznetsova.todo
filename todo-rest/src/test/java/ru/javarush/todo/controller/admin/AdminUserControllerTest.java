package ru.javarush.todo.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.error.ErrorHandler;
import ru.javarush.todo.exception.UserNotFoundException;
import ru.javarush.todo.facade.admin.AdminUserServiceFacade;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private AdminUserServiceFacade adminUserServiceFacade;
    @InjectMocks
    private AdminUserController adminUserController;
    @InjectMocks
    private ObjectMapper mapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminUserController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    void whenGetAllThenReturnListOfUsersAndStatusIsOk() throws Exception {
        Mockito
                .when(adminUserServiceFacade.getAll())
                .thenReturn(createUsers());

        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(4)))

                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].firstName", Is.is("User1")))
                .andExpect(jsonPath("$[0].lastName", Is.is("Userevich1")))
                .andExpect(jsonPath("$[0].email", Is.is("user1@gmail.com")))
                .andExpect(jsonPath("$[0].age", Is.is(31)))
                .andExpect(jsonPath("$[0].roles", Is.is("USER")))

                .andExpect(jsonPath("$[1].id", Is.is(2)))
                .andExpect(jsonPath("$[1].firstName", Is.is("User2")))
                .andExpect(jsonPath("$[1].lastName", Is.is("Userevich2")))
                .andExpect(jsonPath("$[1].email", Is.is("user2@gmail.com")))
                .andExpect(jsonPath("$[1].age", Is.is(32)))
                .andExpect(jsonPath("$[1].roles", Is.is("USER")))

                .andExpect(jsonPath("$[2].id", Is.is(3)))
                .andExpect(jsonPath("$[2].firstName", Is.is("User3")))
                .andExpect(jsonPath("$[2].lastName", Is.is("Userevich3")))
                .andExpect(jsonPath("$[2].email", Is.is("user3@gmail.com")))
                .andExpect(jsonPath("$[2].age", Is.is(33)))
                .andExpect(jsonPath("$[2].roles", Is.is("USER")))

                .andExpect(jsonPath("$[3].id", Is.is(4)))
                .andExpect(jsonPath("$[3].firstName", Is.is("User4")))
                .andExpect(jsonPath("$[3].lastName", Is.is("Userevich4")))
                .andExpect(jsonPath("$[3].email", Is.is("user4@gmail.com")))
                .andExpect(jsonPath("$[3].age", Is.is(34)))
                .andExpect(jsonPath("$[3].roles", Is.is("USER")));
    }

    @Test
    void givenNoUsersWhenGetAllThenReturnEmptyListAndStatusIsOk() throws Exception {
        Mockito
                .when(adminUserServiceFacade.getAll())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Is.is(0)));
    }

    @Test
    void givenValidUserExternalCreatedWhenCreateThenReturnUserResponseDtoAndStatusIsOk() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        UserResponseDto userOutput = UserResponseDto.builder()
                .id(12)
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .roles("USER ADMIN")
                .build();

        Mockito
                .when(adminUserServiceFacade.create(userInput))
                .thenReturn(userOutput);

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(12)))
                .andExpect(jsonPath("$.firstName", Is.is("User")))
                .andExpect(jsonPath("$.lastName", Is.is("Userevich")))
                .andExpect(jsonPath("$.email", Is.is("user@gmail.com")))
                .andExpect(jsonPath("$.age", Is.is(27)))
                .andExpect(jsonPath("$.roles", Is.is("USER ADMIN")));
    }

    @Test
    void givenUserWithBlankNameWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName(" ")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Name must be specify and it shouldn't be blank")));
    }

    @Test
    void givenUserWithoutNameWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Name must be specify and it shouldn't be blank")));
    }

    @Test
    void givenUserWithBlankLastNameWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("    ")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Second name must be specify and it shouldn't be blank")));
    }

    @Test
    void givenUserWithoutLastNameWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Second name must be specify and it shouldn't be blank")));
    }

    @Test
    void givenUserWithBlankEmailWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("           ")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Email is incorrect")));
    }

    @Test
    void givenUserWithoutEmailWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Email must be specify")));
    }

    @ParameterizedTest
    @ValueSource(strings = {" 123456", "@gmail.com", "admin@gmail", "admin.com", "admin", "com.gmail@admin"})
    void givenUserWithIncorrectEmailWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(String email) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email(email)
                .password("123456")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Email is incorrect")));
    }

    @Test
    void givenUserWithBlankPasswordWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("      ")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be specify, no shorter than " +
                        "4 and no longer than 20 characters, shouldn't contain a whitespace")));
    }

    @Test
    void givenUserWithoutPasswordWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be specify, no shorter than " +
                        "4 and no longer than 20 characters, shouldn't contain a whitespace")));
    }

    @Test
    void givenUserWithIncorrectPasswordWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("gh 34 gh")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be specify, no shorter than 4 " +
                        "and no longer than 20 characters, shouldn't contain a whitespace")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"use", "passwordLongerThanTwentySymbols"})
    void givenUserWithIncorrectPasswordWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(String password) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password(password)
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be specify, no shorter than 4 " +
                        "and no longer than 20 characters, shouldn't contain a whitespace")));
    }

    @Test
    void givenUserWithoutAgeWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .password("user")
                .email("user@gmail.com")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Age must be specify")));
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 101})
    void givenUserWithIncorrectPasswordWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(int age) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(age)
                .email("user@gmail.com")
                .password("user")
                .roleNames("USER ADMIN")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("must be between 18 and 100")));
    }

    @Test
    void givenUserWithBlankRoleNamesWhenCreateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("User")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .password("user")
                .roleNames("")
                .build();

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Roles must not be blank")));
    }

    @Test
    void givenValidUserExternalUpdatedWhenUpdateThenReturnUserResponseDtoAndStatusIsOk() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName("AdminNotUser")
                .roleNames("ADMIN")
                .build();

        UserResponseDto userOutput = UserResponseDto.builder()
                .id(12)
                .firstName("AdminNotUser")
                .lastName("Userevich")
                .age(27)
                .email("user@gmail.com")
                .roles("ADMIN")
                .build();

        Mockito
                .when(adminUserServiceFacade.update(12, userInput))
                .thenReturn(userOutput);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Is.is(12)))
                .andExpect(jsonPath("$.firstName", Is.is("AdminNotUser")))
                .andExpect(jsonPath("$.lastName", Is.is("Userevich")))
                .andExpect(jsonPath("$.email", Is.is("user@gmail.com")))
                .andExpect(jsonPath("$.age", Is.is(27)))
                .andExpect(jsonPath("$.roles", Is.is("ADMIN")));
    }

    @Test
    void givenUserExternalUpdatedWithBlankNameWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .firstName(" ")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Name must not be blank")));
    }

    @Test
    void givenUserExternalUpdatedWithBlankLastNameWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .lastName("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Second name must not be blank")));
    }

    @Test
    void givenUserExternalUpdatedWithBlankEmailWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .email("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Email is incorrect")));
    }

    @ParameterizedTest
    @ValueSource(strings = {" 123456", "@outlook.com", "user@outlook.co", "admin@gmai.com", "admin", "com.gmail@admin"})
    void givenUserExternalUpdatedWithIncorrectEmailWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(String email) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .email(email)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Email is incorrect")));
    }

    @Test
    void givenUserExternalUpdatedWithBlankPasswordWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .password("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be no shorter than 4 and no" +
                        " longer than 20 characters, must not contain a whitespace")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user user", "use", "passwordLongerThanTwentySymbols"})
    void givenUserExternalUpdatedWithIncorrectPasswordWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(String password) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .password(password)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("The password must be no shorter than 4 and no" +
                        " longer than 20 characters, must not contain a whitespace")));
    }

    @ParameterizedTest
    @ValueSource(ints = {17, 101})
    void givenUserExternalUpdatedWithIncorrectPasswordWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest(int age) throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .age(age)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("must be between 18 and 100")));
    }

    @Test
    void givenUserExternalUpdatedWithBlankRoleNamesWhenUpdateThenThrowsMethodArgumentNotValidExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .roleNames("")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the object field")))
                .andExpect(jsonPath("$.message", Is.is("Roles must not be blank")));
    }

    @Test
    void givenIdOfNotExistsUserWhenUpdateThenThrowsUserNotFoundExceptionAndStatusIsNotFound() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .roleNames("ADMIN")
                .build();

        Mockito
                .when(adminUserServiceFacade.update(15, userInput))
                .thenThrow(new UserNotFoundException("User with id = 15 doesn't exist"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(jsonPath("$.reason", Is.is("User doesn't exist")))
                .andExpect(jsonPath("$.message", Is.is("User with id = 15 doesn't exist")));
    }

    @Test
    void givenIncorrectIdWhenUpdateThenThrowsConstraintViolationExceptionAndStatusIsBadRequest() throws Exception {
        UserRequestDto userInput = UserRequestDto.builder()
                .roleNames("ADMIN")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/users/user/h12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userInput)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the url parameter")))
                .andExpect(jsonPath("$.message", Is.is("Incorrect url path variable - \"h12\"")));
    }

    @Test
    void whenSuccessfullyDeleteThenStatusIsOk() throws Exception {
        Mockito
                .doNothing()
                .when(adminUserServiceFacade).delete(12);

        mockMvc.perform(MockMvcRequestBuilders.delete(URI.create("/api/admin/users/user/12")))
                .andExpect(status().isOk());
    }

    @Test
    void givenIdOfNotExistsUserWhenDeleteThenThrowsUserNotFoundExceptionAndStatusIsNotFound() throws Exception {
        Mockito
                .doThrow(new UserNotFoundException("User with id = 12 doesn't exist"))
                .when(adminUserServiceFacade).delete(12);

        mockMvc.perform(MockMvcRequestBuilders.delete(URI.create("/api/admin/users/user/12")))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(jsonPath("$.message", Is.is("User with id = 12 doesn't exist")));
    }

    @Test
    void givenIncorrectIdWhenDeleteThenThrowsConstraintViolationExceptionAndStatusIsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI.create("/api/admin/users/user/rtry")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(jsonPath("$.reason", Is.is("Error in the url parameter")))
                .andExpect(jsonPath("$.message", Is.is("Incorrect url path variable - \"rtry\"")));
    }

    private List<UserResponseDto> createUsers() {
        return IntStream.range(1, 5)
                .mapToObj(number -> UserResponseDto.builder()
                        .id(number)
                        .firstName("User%s".formatted(number))
                        .lastName("Userevich%s".formatted(number))
                        .age(30 + number)
                        .email("user%s@gmail.com".formatted(number))
                        .roles("USER")
                        .build())
                .collect(Collectors.toList());
    }
}