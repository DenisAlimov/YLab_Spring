package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.ValidationException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final UserStorage userStorage; //не могу создавать как ProjectRepository, тк метод в UserStorage

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper, UserStorage userStorage) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.userStorage = userStorage;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        if (userBookRequest.getUserRequest().getFullName() == null) {
            throw new ValidationException("User name shouldn't be empty");
        }
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUserDto = userService.createUser(userDto);
        log.info("Created user: {}", createdUserDto);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUserDto.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        userStorage.addBooks(createdUserDto.getId(), bookIdList);

        return UserBookResponse.builder()
                .userId(createdUserDto.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book update request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        log.info("Updated user: {}", updatedUserDto);

        List<Long> bookIdList = userBookRequest.getBookRequests()//DRY страдает, вынести лучше в метод?
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userId))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        if (!userStorage.getUsers().containsKey(updatedUserDto.getId())) {
            throw new NotFoundException("Can't find user by this id");
        }

        userStorage.updateUserBooks(bookIdList, userId);

        return UserBookResponse.builder()
                .userId(updatedUserDto.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        return UserBookResponse.builder()
                .userId(userId)
                .booksIdList(userStorage.getBooksIdList(userId))
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        userStorage.removeItemById(userId);
    }
}