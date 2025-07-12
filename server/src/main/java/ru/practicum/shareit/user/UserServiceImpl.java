package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto user) {
        if (userRepository.existsByEmail(user.getEmail()) || user.getEmail().isBlank()) {
            throw new EmailExistException(String.format("Почта %s уже существует или неверно указана", user.getEmail()));
        }
        User newUser = userRepository.save(UserMapper.toUser(user));
        //log.info("Создан пользователь: {}", newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto update(Long id, UserDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistException(String.format("Почта %s уже существует или неверно указана", user.getEmail()));
        }
        User updateUser = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id = %s не найден", id)));
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }
        userRepository.save(updateUser);
        //log.info("Обновлен пользователь: {}", updateUser);
        return UserMapper.toUserDto(updateUser);
    }

    @Override
    public UserDto get(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден",id))));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
        //log.info("Удален пользователь с id = {}", id);
    }
}