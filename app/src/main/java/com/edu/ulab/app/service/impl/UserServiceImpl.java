package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserDto user = userStorage.store(userDto);
        log.info("User created: {}", user);
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        return user;
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Can't find user by this id");
        }
        UserDto user = userStorage.update(userDto, id);
        log.info("User updated: {}", user);
        return user;
    }

    @Override
    public UserDto getUserById(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Can't find user by this id");
        }
        UserDto user = userStorage.find(id);
        log.info("Got user: {}", user);
        return user;
    }

    @Override
    public void deleteUserById(long id) {
        userStorage.removeItemById(id);
        log.info("User deleted by id = {}", id);
    }
}
