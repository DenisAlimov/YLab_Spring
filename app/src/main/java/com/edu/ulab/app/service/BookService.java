package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

public interface BookService {
    BookDto createBook(BookDto userDto);

    BookDto updateBook(BookDto userDto, long id);

    BookDto getBookById(long id);

    void deleteBookById(long id);
}
