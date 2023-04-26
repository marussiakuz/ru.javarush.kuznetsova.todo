package ru.javarush.todo.converter.user;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.javarush.todo.config.MapperConfig;
import ru.javarush.todo.converter.DtoMapper;
import ru.javarush.todo.converter.EntityMapper;
import ru.javarush.todo.converter.role.RoleMapper;
import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.model.User;
import ru.javarush.todo.service.RoleService;

@Mapper(config = MapperConfig.class, uses = {RoleMapper.class})
public abstract class UserMapper implements DtoMapper<UserResponseDto, User>, EntityMapper<UserRequestDto, User> {

    protected RoleService roleService;
    protected BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(target = "roles",
            expression = "java(new java.util.HashSet(roleService.getAllByName(java.util.Arrays.stream(userRequestDto" +
                    ".getRoleNames().split(\",\")).collect(java.util.stream.Collectors.toSet()))))")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userRequestDto.getPassword()))")
    @Override
    public abstract User toEntity(UserRequestDto userRequestDto);

    @Mapping(target = "roles",
            expression = "java(new java.util.HashSet(roleService.getAllByName(java.util.Arrays.stream(userRequestDto" +
                    ".getRoleNames().split(\",\")).collect(java.util.stream.Collectors.toSet()))))")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userRequestDto.getPassword()))")
    @Override
    public abstract void updateEntityFromDto(UserRequestDto userRequestDto, @MappingTarget User user);
}
