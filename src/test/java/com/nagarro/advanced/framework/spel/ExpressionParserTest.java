package com.nagarro.advanced.framework.spel;

import com.nagarro.advanced.framework.persistence.entity.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionParserTest {

    private static ExpressionParser parser;
    private static Book book;

    @BeforeAll
    static void setUp() {
        book = new Book();
        book.setIsbn("237rbf74fe");
        book.setAuthor("Liviu Rebreanu");
        book.setTitle("Ion");
        book.setPrice(BigDecimal.valueOf(25));
        book.setDetails("Bestseller");
        parser = new ExpressionParser();
    }

    @Test
    void shouldReturnBookAuthorWhenParseAuthor() {
        //GIVEN
        String authorName = "Liviu Rebreanu";

        //WHEN
        String actualAuthorName = parser.parseAuthor(book);

        //THEN
        assertEquals(authorName, actualAuthorName);
    }
}
