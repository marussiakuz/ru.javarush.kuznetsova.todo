package ru.javarush.todo.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

    private long id;
    private String title;
    private String deadline;
    private String status;

}
