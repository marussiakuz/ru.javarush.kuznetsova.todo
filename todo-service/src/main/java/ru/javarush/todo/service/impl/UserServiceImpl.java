package ru.javarush.todo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.todo.dto.request.UserRequestDto;
import ru.javarush.todo.dto.response.UserResponseDto;
import ru.javarush.todo.exception.UserNotFoundException;
import ru.javarush.todo.converter.user.UserMapper;
import ru.javarush.todo.repository.UserRepository;
import ru.javarush.todo.model.User;
import ru.javarush.todo.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAll() {
        return userMapper.toDto(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getByEmail(String email) {
        return userMapper.toDto(getByEmailOrThrowException(email));
    }

    @Transactional
    @Override
    public UserResponseDto create(UserRequestDto user) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)));
    }

    @Transactional
    @Override
    public UserResponseDto update(long id, UserRequestDto userDto) {
        User user = getByIdOrThrowException(id);
        userMapper.updateEntityFromDto(userDto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.delete(getByIdOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getByEmailOrThrowException(email);
    }

    private User getByIdOrThrowException(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id=%d not found", id)));
    }

    private User getByEmailOrThrowException(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("user with email=%s not found", email)));
    }
}
