package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookStorage bookStorage;

    public BookServiceImpl(BookStorage bookStorage) {
        this.bookStorage = bookStorage;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        return bookStorage.store(bookDto);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, long id) {
        if (!bookStorage.getBooks().containsKey(id)) {
            throw new NotFoundException("Can't find element by this id");
        }
        BookDto book = bookStorage.update(bookDto, id);
        log.info("Book updated: {}", book);
        return book;
    }

    @Override
    public BookDto getBookById(long id) {
        if (!bookStorage.getBooks().containsKey(id)) {
            throw new NotFoundException("Can't find element by this id");
        }
        BookDto book = bookStorage.find(id);
        log.info("Got book: {}", book);
        return book;
    }

    @Override
    public void deleteBookById(long id) {
        bookStorage.removeItemById(id);
        log.info("Book deleted by id = {}", id);
    }
}