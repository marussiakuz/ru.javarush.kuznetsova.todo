package ru.javarush.todo.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
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
