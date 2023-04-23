package ru.javarush.todo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.*;
import ru.javarush.todo.model.Status;
import ru.javarush.todo.validation.dto.IsFuture;
import ru.javarush.todo.validation.dto.NullOrNotBlank;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    @NullOrNotBlank
    private String title;
    @NullOrNotBlank
    private String description;
    @IsFuture
    private LocalDate deadline;
    private Status status;

}
