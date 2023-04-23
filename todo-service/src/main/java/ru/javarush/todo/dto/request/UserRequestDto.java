package ru.javarush.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import ru.javarush.todo.validation.dto.IsPasswordValid;
import ru.javarush.todo.validation.dto.NullOrNotBlank;

@ToString
@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NullOrNotBlank
    private String firstName;
    @NullOrNotBlank
    private String lastName;
    @Email
    private String email;
    @IsPasswordValid
    private String password;
    @Min(18)
    private Integer age;
    @NullOrNotBlank
    private String roleNames;

}
