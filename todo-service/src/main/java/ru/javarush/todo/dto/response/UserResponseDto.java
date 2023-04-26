package ru.javarush.todo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class UserResponseDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private String roles;
}
