package ru.javarush.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.*;
import ru.javarush.todo.dto.OnCreate;
import ru.javarush.todo.dto.OnUpdate;
import ru.javarush.todo.model.Status;
import ru.javarush.todo.validation.dto.IsFuture;
import ru.javarush.todo.validation.dto.NullOrNotBlank;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    @NotBlank(message = "Title must be specify and it shouldn't be blank", groups = OnCreate.class)
    @NullOrNotBlank(message = "Title must not be blank", groups = OnUpdate.class)
    private String title;

    @NotBlank(message = "Description must be specify and it shouldn't be blank", groups = OnCreate.class)
    @NullOrNotBlank(message = "Description must not be blank", groups = OnUpdate.class)
    private String description;

    @NotNull(message = "Deadline must be specify", groups = OnCreate.class)
    @IsFuture(message = "Deadline must be in the future", isNullable = true, groups = Default.class)
    private LocalDate deadline;

    private Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskRequestDto that)) return false;

        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(deadline, that.deadline)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (deadline != null ? deadline.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
