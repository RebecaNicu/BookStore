package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.persistence.repository.BookRepository;
import com.nagarro.advanced.framework.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private static final String NONEXISTENT_CATEGORY = "The category doesn't exist!";

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void saveShouldSaveBookForValidInput() {
        //given
        Book expectedBook = Book.builder().build();

        //when
        when(bookRepository.save(expectedBook)).thenReturn(expectedBook);
        Book actualBook = bookService.save(expectedBook);

        //then
        assertEquals(expectedBook, actualBook);
    }

    @Test
    void findByIsbnShouldReturnBookForValidIsbn() {
        //given
        Book expectedBook = Book.builder().build();

        //when
        when(bookRepository.findByIsbn(expectedBook.getIsbn())).thenReturn(Optional.of(expectedBook));
        Optional<Book> actualBook = bookService.findByIsbn(expectedBook.getIsbn());

        //then
        assertEquals(Optional.of(expectedBook), actualBook);
    }

    @Test
    void deleteByIsbnShouldDeleteBookForValidIsbn() {
        //given
        Book expectedBook = Book.builder().build();

        //when
        when(bookRepository.findByIsbn(expectedBook.getIsbn())).thenReturn(Optional.of(expectedBook));
        bookService.deleteByIsbn(expectedBook.getIsbn());

        //then
        verify(bookRepository).delete(expectedBook);
    }

    @Test
    void findByTitleShouldReturnBookForValidTitle() {
        //given
        Book expectedBook = Book.builder().build();

        //when
        when(bookRepository.findByTitle(expectedBook.getTitle())).thenReturn(Optional.of(expectedBook));
        Optional<Book> actualBook = bookService.findByTitle(expectedBook.getTitle());

        //then
        assertEquals(Optional.of(expectedBook), actualBook);
    }

    @Test
    void updateShouldUpdateBookByIsbnIfBookTitleDoesNotExist() {
        //given
        Category oldCategory = new Category();
        oldCategory.setName("c1");
        Book oldBook = Book.builder().title("Ion").author("Liviu Rebreanu")
                .details("details").price(BigDecimal.valueOf(99.9)).category(oldCategory).build();
        Category newCategory = new Category();
        newCategory.setName("valid category");
        Book newBook = Book.builder().title(oldBook.getTitle()).author(oldBook.getAuthor())
                .details(oldBook.getDetails()).price(oldBook.getPrice()).category(newCategory).build();

        //when
        when(bookRepository.findByIsbn(oldBook.getIsbn())).thenReturn(Optional.of(oldBook));
        when(categoryRepository.findByUuid(newBook.getCategory().getUuid())).thenReturn(Optional.of(oldCategory));
        bookService.update(oldBook.getIsbn(), newBook);

        //then
        verify(bookRepository).save(oldBook);
    }

    @Test
    void updateShouldThrowAnExceptionWhenTheNewCategoryIsNotValid() {
        //given
        Category oldCategory = new Category();
        oldCategory.setName("c1");
        Book oldBook = Book.builder().title("Cinci paini").author("Ion Creanga")
                .details("details").price(BigDecimal.valueOf(99.9)).category(oldCategory).build();
        Category invalidCategory = new Category();
        invalidCategory.setName("Non existent category");
        Book newBook = Book.builder().title(oldBook.getTitle()).author(oldBook.getAuthor())
                .details(oldBook.getDetails()).price(oldBook.getPrice()).category(invalidCategory).build();

        //when
        when(bookRepository.findByIsbn(oldBook.getIsbn())).thenReturn(Optional.of(oldBook));
        when(categoryRepository.findByUuid(newBook.getCategory().getUuid())).thenReturn(Optional.empty());
        AppException appException = assertThrows(AppException.class, ()
                -> bookService.update(oldBook.getIsbn(), newBook));

        //then
        assertEquals(NONEXISTENT_CATEGORY, appException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, appException.getHttpStatus());
    }
}
