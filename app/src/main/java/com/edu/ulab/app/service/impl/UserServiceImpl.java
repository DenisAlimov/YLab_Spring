package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.ProjectRepository;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final ProjectRepository<UserDto> userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // сгенерировать идентификатор
        // создать пользователя
        // вернуть сохраненного пользователя со всеми необходимыми полями id
        return userStorage.store(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long id) {
        return userStorage.update(userDto, id);
    }

    @Override
    public UserDto getUserById(long id) {
        return userStorage.find(id);
    }

    @Override
    public void deleteUserById(long id) {
        userStorage.removeItemById(id);
    }
}
