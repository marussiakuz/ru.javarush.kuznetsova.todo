package ru.javarush.todo.service;

import ru.javarush.todo.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    List<Role> getAllByName(Set<String> roles);
}
