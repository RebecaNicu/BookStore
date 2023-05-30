package com.nagarro.advanced.framework.validation;

import com.nagarro.advanced.framework.persistence.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookValidatorTest {
    private static BookValidator validator;

    @BeforeAll
    static void setUp() {
        validator = new BookValidator();
    }

    @Test
    void shouldReturnEmptyTitleWhenBookHasNoTitle() {
        //GIVEN
        Book book = new Book();
        book.setIsbn("237rbf74fe");
        book.setAuthor("Liviu Rebreanu");
        book.setPrice(BigDecimal.valueOf(25));

        book.setTitle("");
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Title cannot be empty");

        //WHEN
        List<String> actualErrors = validator.getErrorMessage(book);

        //THEN
        assertEquals(expectedErrors, actualErrors);

    }

    @Test
    void shouldReturnEmptyAuthorWhenBookHasNoAuthor() {
        //GIVEN
        Book book = new Book();
        book.setIsbn("237rbf74fe");
        book.setTitle("Ion");
        book.setPrice(BigDecimal.valueOf(25));

        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Author cannot be empty");

        //WHEN
        List<String> actualErrors = validator.getErrorMessage(book);

        //THEN
        assertEquals(expectedErrors, actualErrors);

    }

    @Test
    void shouldReturnNegativePriceWhenBookInvalidPrice() {
        //GIVEN
        Book book = new Book();
        book.setIsbn("237rbf74fe");
        book.setAuthor("Liviu Rebreanu");
        book.setTitle("Ion");
        book.setPrice(BigDecimal.valueOf(-25));
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Price cannot be negative");

        //WHEN
        List<String> actualErrors = validator.getErrorMessage(book);

        //THEN
        assertEquals(expectedErrors, actualErrors);
    }

    @Test
    void shouldReturnRequiredIsbnWhenBookHasNoIsbn() {
        //GIVEN
        Book book = new Book();
        book.setAuthor("Liviu Rebreanu");
        book.setTitle("Ion");
        book.setPrice(BigDecimal.valueOf(25));
        List<String> expectedErrors = new ArrayList<>();
        expectedErrors.add("Isbn required");

        //WHEN
        List<String> actualErrors = validator.getErrorMessage(book);

        //THEN
        assertEquals(expectedErrors, actualErrors);
    }
}
