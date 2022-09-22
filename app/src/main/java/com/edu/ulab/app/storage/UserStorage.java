package com.edu.ulab.app.storage;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class UserStorage implements ProjectRepository<UserDto> {

    private final Map<Long, UserDto> users = new HashMap<>();

    private final Map<Long, List<Long>> userBooks = new HashMap<>();
    private final AtomicLong atomicLong = new AtomicLong();
    private final ProjectRepository<BookDto> bookStorage;

    public UserStorage(ProjectRepository<BookDto> bookStorage) {
        this.bookStorage = bookStorage;
    }

    @Override
    public UserDto store(UserDto item) {
        Long userId = atomicLong.incrementAndGet();
        item.setId(userId);
        users.putIfAbsent(userId, item);
        log.info("User added to user storage: {}", item);
        return item;
    }

    @Override
    public UserDto find(long id) {
        UserDto userDto = users.get(id);
        return userDto;
    }

    @Override
    public void removeItemById(long id) {
        users.remove(id);
        if (userBooks.get(id) != null && !userBooks.get(id).isEmpty()) {
            userBooks.get(id).forEach(bookStorage::removeItemById);
        }
        userBooks.remove(id);
    }

    @Override
    public UserDto update(UserDto item, long id) {
        return users.put(id, item);
    }

    public void updateUserBooks(List<Long> bookIdList, long id) {
        userBooks.replace(id, bookIdList);
    }


    public void addBooks(long userId, List<Long> bookId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Can't find element by this id");
        }
        userBooks.put(userId, bookId);
    }

    public List<Long> getBooksIdList(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Can't find element by this id");
        }
        return userBooks.get(userId);
    }

    public Map<Long, UserDto> getUsers() {
        return users;
    }
}