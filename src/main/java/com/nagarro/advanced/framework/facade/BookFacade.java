package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.service.BookService;
import com.nagarro.advanced.framework.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookFacade {

    private static final String THE_BOOK_ALREADY_EXISTS = "The book already exists!";
    private static final String NONEXISTENT_BOOK = "The book doesn't exist!";

    private final BookService bookService;

    private final CategoryService categoryService;

    private final Converter<Book, BookDto> bookConverter;

    @Autowired
    public BookFacade(BookService bookService, CategoryService categoryService, Converter<Book, BookDto> bookConverter) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.bookConverter = bookConverter;
    }

    public BookDto saveBook(BookDto bookDto) {
        Optional<Book> optionalTitleBook = bookService.findByTitle(bookDto.getTitle());
        Optional<Book> optionalAuthorBook = bookService.findByAuthor(bookDto.getAuthor());
        if (optionalAuthorBook.isPresent() && optionalTitleBook.isPresent()) {
            throw new AppException(THE_BOOK_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }

        String categoryUuid = bookDto.getCategoryUuid();
        Book book = bookConverter.toEntity(bookDto);
        Optional<Category> category = Optional.of(categoryService.getCategoryByUuid(categoryUuid));
        category.get().addBook(book);

        return bookConverter.toDto(bookService.save(book));
    }

    public BookDto findBookByIsbn(String isbn) {
        return bookService.findByIsbn(isbn).map(bookConverter::toDto)
                .orElseThrow(() -> new AppException(NONEXISTENT_BOOK, HttpStatus.NOT_FOUND));
    }

    public void deleteBookByIsbn(String isbn) {
        if (bookService.findByIsbn(isbn).isPresent()) {
            bookService.deleteByIsbn(isbn);
        } else {
            throw new AppException(NONEXISTENT_BOOK, HttpStatus.NOT_FOUND);
        }
    }

    public void updateBook(String isbn, BookDto bookDto) {
        bookService.update(isbn, bookConverter.toEntity(bookDto));
    }
}
