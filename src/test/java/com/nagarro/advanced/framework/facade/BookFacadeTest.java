package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.service.BookService;
import com.nagarro.advanced.framework.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

    private static final String NONEXISTENT_BOOK = "The book doesn't exist!";

    @Mock
    private Converter<Book, BookDto> bookConverter;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookFacade bookFacade;

    @Test
    void saveBookShouldSaveBookToDataBaseForValidInput() {
        //given
        List<Book> books = new ArrayList<>();
        books.add(Book.builder().build());
        Category category = Category.builder().uuid("29e45e7d-3e34-43df-9366-91c66a8cc9ae").name("c2")
                .books(books).build();
        Book expectedBook = Book.builder().isbn("33e45e7d-3e34-43df-9366-91c66a8cc9ae").title("Ion")
                .author("Rebreanu").price(BigDecimal.valueOf(37.12)).details("details").build();
        BookDto expectedBookDto = BookDto.builder().isbn("33e45e7d-3e34-43df-9366-91c66a8cc9ae").title("Ion")
                .author("Rebreanu").price(BigDecimal.valueOf(37.12)).details("details")
                .categoryUuid("29e45e7d-3e34-43df-9366-91c66a8cc9ae").build();

        //when
        when(bookConverter.toEntity(expectedBookDto)).thenReturn(expectedBook);
        when(categoryService.getCategoryByUuid(expectedBookDto.getCategoryUuid())).thenReturn(category);
        when(bookService.save(expectedBook)).thenReturn(expectedBook);
        when(bookConverter.toDto(expectedBook)).thenReturn(expectedBookDto);
        BookDto actualBookDto = bookFacade.saveBook(expectedBookDto);

        //then
        assertEquals(expectedBookDto.getTitle(), actualBookDto.getTitle());
        assertEquals(expectedBookDto.getAuthor(), actualBookDto.getAuthor());
        assertEquals(expectedBookDto.getPrice(), actualBookDto.getPrice());
        assertEquals(expectedBookDto.getDetails(), actualBookDto.getDetails());
        assertEquals(expectedBookDto.getCategoryUuid(), actualBookDto.getCategoryUuid());
        assertEquals(expectedBookDto.getIsbn(), actualBookDto.getIsbn());
    }

    @Test
    void findBookByIsbnShouldThrowExceptionWhenBookDoesNotExist() {
        //given
        String nonExistentUuid = UUID.randomUUID().toString();

        //when
        when(bookService.findByIsbn(nonExistentUuid)).thenReturn(Optional.empty());
        AppException appException = assertThrows(AppException.class, ()
                -> bookFacade.findBookByIsbn(nonExistentUuid));

        //then
        assertEquals(NONEXISTENT_BOOK, appException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, appException.getHttpStatus());
    }

    @Test
    void findBookByIsbnShouldReturnBookForValidIsbn() {
        //given
        Book expectedBook = Book.builder().build();
        BookDto expectedBookDto = BookDto.builder().build();

        //when
        when(bookService.findByIsbn(expectedBook.getIsbn())).thenReturn(Optional.of(expectedBook));
        when(bookConverter.toDto(expectedBook)).thenReturn(expectedBookDto);
        BookDto actualBookDto = bookFacade.findBookByIsbn(expectedBook.getIsbn());

        //then
        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    void deleteBookByIsbnShouldDeleteBookFromDataBaseForExistentIsbn() {
        //given
        Book expectedBook = Book.builder().build();
        willDoNothing().given(bookService).deleteByIsbn(expectedBook.getIsbn());

        //when
        when(bookService.findByIsbn(expectedBook.getIsbn())).thenReturn(Optional.of(expectedBook));
        bookFacade.deleteBookByIsbn(expectedBook.getIsbn());

        //then
        verify(bookService).deleteByIsbn(expectedBook.getIsbn());
    }

    @Test
    void deleteBookByIsbnShouldThrowExceptionForNonexistentIsbn() {
        String nonExistentUuid = UUID.randomUUID().toString();

        //when
        when(bookService.findByIsbn(nonExistentUuid)).thenReturn(Optional.empty());
        AppException appException = assertThrows(AppException.class, ()
                -> bookFacade.findBookByIsbn(nonExistentUuid));

        //then
        assertEquals(NONEXISTENT_BOOK, appException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, appException.getHttpStatus());
    }

    @Test
    void updateBookShouldUpdateBookForValidInput() {
        //given
        Category category = Category.builder().build();
        Book book = Book.builder().title("Ion").author("Liviu Rebreanu")
                .details("details").price(BigDecimal.valueOf(99.9)).category(category).build();
        Book newBook = Book.builder().title("Casa").author("Arghezi")
                .details("details").price(BigDecimal.valueOf(122.2)).category(category).build();
        BookDto newBookDto = BookDto.builder().build();

        //when
        when(bookConverter.toEntity(newBookDto)).thenReturn(newBook);
        bookFacade.updateBook(book.getIsbn(), newBookDto);

        //then
        verify(bookService).update(book.getIsbn(), newBook);
    }
}
