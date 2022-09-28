package com.edu.ulab.app.storage;

import com.edu.ulab.app.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class BookStorage implements ProjectRepository<BookDto> {

    private final Map<Long, BookDto> books = new HashMap<>();
    private final AtomicLong atomicLong = new AtomicLong();

    @Override
    public BookDto store(BookDto item) {
        Long bookId = atomicLong.incrementAndGet();
        item.setId(bookId);
        books.putIfAbsent(bookId, item);
        log.info("Book added to book storage: {}", item);
        return item;
    }

    @Override
    public BookDto find(long id) {
        BookDto bookDto = books.get(id);
        return bookDto;
    }

    @Override
    public void removeItemById(long id) {
        books.remove(id);
    }

    @Override
    public BookDto update(BookDto item, long id) {
        return books.put(id, item);
    }

    public Map<Long, BookDto> getBooks() {
        return books;
    }
}

