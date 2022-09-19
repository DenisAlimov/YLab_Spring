package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import com.edu.ulab.app.storage.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final ProjectRepository<BookDto> bookStorage;

    public BookServiceImpl(BookStorage bookStorage) {
        this.bookStorage = bookStorage;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        return bookStorage.store(bookDto);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, long id) {
        return bookStorage.update(bookDto, id);
    }

    @Override
    public BookDto getBookById(long id) {
        return bookStorage.find(id);
    }

    @Override
    public void deleteBookById(long id) {
        bookStorage.removeItemById(id);
    }
}