package ru.javarush.todo.converter.role;

import org.mapstruct.Mapper;
import ru.javarush.todo.config.MapperConfig;
import ru.javarush.todo.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {

    default String map(Set<Role> roles) {
        return isNull(roles) ? null : roles.stream()
                .map(Role::getName)
                .map(name -> name.replace("ROLE_", ""))
                .sorted()
                .collect(Collectors.joining(" "));
    }

}
