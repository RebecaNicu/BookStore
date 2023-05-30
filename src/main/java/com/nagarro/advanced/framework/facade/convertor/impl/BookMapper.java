package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper implements Converter<Book, BookDto> {
    private final ModelMapper modelMapper;

    @Override
    public Book toEntity(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    @Override
    public BookDto toDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }
}