package ru.javarush.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.*;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Range;
import ru.javarush.todo.dto.OnCreate;
import ru.javarush.todo.dto.OnUpdate;
import ru.javarush.todo.validation.dto.IsPasswordValid;
import ru.javarush.todo.validation.dto.NullOrNotBlank;

import java.util.Objects;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Name must be specify and it shouldn't be blank", groups = OnCreate.class)
    @NullOrNotBlank(message = "Name must not be blank", groups = OnUpdate.class)
    private String firstName;

    @NotBlank(message = "Second name must be specify and it shouldn't be blank", groups = OnCreate.class)
    @NullOrNotBlank(message = "Second name must not be blank", groups = OnUpdate.class)
    private String lastName;

    @NotNull(message = "Email must be specify", groups = OnCreate.class)
    @Email(message = "Email is incorrect", regexp = ".+@(gmail|outlook|mail)\\.com$", groups = Default.class)
    private String email;

    @IsPasswordValid(isNullable = false, message = "The password must be specify, no shorter than 4 and no longer than " +
            "20 characters, shouldn't contain a whitespace", groups = OnCreate.class)
    @IsPasswordValid(message = "The password must be no shorter than 4 and no longer than 20 characters, must not " +
            "contain a whitespace", groups = OnUpdate.class)
    private String password;

    @NotNull(message = "Age must be specify", groups = OnCreate.class)
    @Range(min = 18, max = 100, groups = Default.class)
    private Integer age;

    @NullOrNotBlank(message = "Roles must not be blank", groups = Default.class)
    private String roleNames;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRequestDto that)) return false;

        if (!Objects.equals(firstName, that.firstName)) return false;
        if (!Objects.equals(lastName, that.lastName)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(age, that.age)) return false;
        return Objects.equals(roleNames, that.roleNames);
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (roleNames != null ? roleNames.hashCode() : 0);
        return result;
    }
}
