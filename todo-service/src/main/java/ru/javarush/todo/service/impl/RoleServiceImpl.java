package ru.javarush.todo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todo.model.Role;
import ru.javarush.todo.repository.RoleRepository;
import ru.javarush.todo.service.RoleService;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Role> getAllByName(Set<String> roles) {
        return roleRepository.getAllByNames(roles);
    }
}
