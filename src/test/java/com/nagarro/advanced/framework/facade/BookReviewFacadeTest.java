package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.BookReviewDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.impl.BookReviewMapper;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.BookReview;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.service.BookReviewService;
import com.nagarro.advanced.framework.service.BookService;
import com.nagarro.advanced.framework.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReviewFacadeTest {

    private static final String BOOK_OR_USER_DOES_NOT_EXIST = "There is no book or user with this id!";
    private static final String BOOK_DOES_NOT_EXIST = "There is no book with this id!";
    private static final String BOOK_ISBN = "15e45e7d-3e34-43df-9366-91c66a8cc9ae";
    private static final String REVIEW_UUID = "15e45e7d-4r34-43bn-9377-91c63a8cc9ae";

    @InjectMocks
    private BookReviewFacade reviewFacade;

    @Mock
    private BookReviewService reviewService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BookReviewMapper reviewConverter;

    @Test
    void shouldReturnReviewWhenAddReview() {
        //given
        List<BookReview> reviews = new ArrayList<>();
        reviews.add(BookReview.builder().build());
        User user = User.builder().reviews(reviews).build();
        Book book = Book.builder().isbn(BOOK_ISBN).reviews(reviews).build();
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid(REVIEW_UUID).build();
        BookReview reviewEntity = BookReview.builder().uuid(REVIEW_UUID).id(3L).build();

        //when
        when(reviewService.addReview(reviewEntity)).thenReturn(reviewEntity);
        when(bookService.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
        when(userService.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(reviewConverter.toEntity(expectedReviewDto)).thenReturn(reviewEntity);
        when(reviewConverter.toDto(reviewEntity)).thenReturn(expectedReviewDto);
        BookReviewDto actualReviewDto = reviewFacade.addReview(expectedReviewDto, book.getIsbn(), user.getUuid());

        //then
        assertEquals(expectedReviewDto, actualReviewDto);
    }

    @Test
    void deleteBookByIsbnShouldDeleteBookFromDataBaseForExistentIsbn() {
        //given
        BookReview review = BookReview.builder().uuid("15e45e7d-4r34-43df-9377-91c63a8cc9ae").build();
        List<BookReview> reviews = new ArrayList<>();
        reviews.add(review);
        User user = User.builder().build();
        Book book = Book.builder().reviews(reviews).build();

        //when
        when(bookService.findByIsbn(BOOK_ISBN)).thenReturn(Optional.of(book));
        when(userService.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(reviewService.findByUuid("15e45e7d-4r34-43df-9377-91c63a8cc9ae")).thenReturn(Optional.of(review));
        reviewFacade.deleteReview(BOOK_ISBN, user.getUuid(), review.getUuid());

        //then
        verify(reviewService).deleteByUuid(review.getUuid());
    }

    @Test
    void shouldReturnReviewsWhenGetAll() {
        //given
        Book book = Book.builder().build();
        String bookIsbn = book.getIsbn();
        List<BookReview> expectedReviews = List.of(BookReview.builder().build());
        List<BookReviewDto> expectedReviewListDto = List.of(BookReviewDto.builder().build());

        //when
        when(bookService.findByIsbn(bookIsbn)).thenReturn(Optional.of(book));
        when(reviewConverter.convertToReviewList(expectedReviews)).thenReturn(expectedReviewListDto);
        when(reviewService.findAll(bookIsbn)).thenReturn(expectedReviews);
        List<BookReviewDto> actualReviewsDto = reviewFacade.getAll(bookIsbn);

        //then
        assertEquals(expectedReviewListDto, actualReviewsDto);
    }

    @Test
    void shouldReturnUpdatedReviewWhenUpdate() {
        //given
        User user = User.builder().build();
        Book book = Book.builder().build();
        String bookIsbn = book.getIsbn();
        BookReview review = BookReview.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();

        //when
        when(bookService.findByIsbn(bookIsbn)).thenReturn(Optional.of(book));
        when(userService.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(reviewConverter.toEntity(expectedReviewDto)).thenReturn(review);
        reviewFacade.updateReview(bookIsbn, user.getUuid(), "15e45e7d-4r34-43df-9366-91c63a8bb9ae", expectedReviewDto);

        //then
        verify(reviewService).updateReview("15e45e7d-4r34-43df-9366-91c63a8bb9ae", review);
    }

    @Test
    void shouldReturn404WhenUpdateBookIsbnNotExist() {
        //given
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8wq9ae";
        String reviewUuid = "15e45e7d-4r34-43df-9377-91c63a8cc9ae";
        User user = User.builder().build();
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.updateReview(bookIsbn, user.getUuid(), reviewUuid, expectedReviewDto));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenUpdateUserNotExist() {
        //given
        String reviewUuid = "15e45e7d-4r34-43df-9377-91c63a8cc9ae";
        User user = User.builder().build();
        Book book = Book.builder().build();
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();

        //when
        when(bookService.findByIsbn(BOOK_ISBN)).thenReturn(Optional.of(book));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.updateReview(BOOK_ISBN, user.getUuid(), reviewUuid, expectedReviewDto));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenAddReviewBookIsbnNotExist() {
        //given
        User user = User.builder().build();
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8wq9ae";
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.addReview(expectedReviewDto, bookIsbn, user.getUuid()));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenAddReviewUserNotExist() {
        //given
        BookReviewDto expectedReviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63a8bb9ae").build();
        User user = User.builder().build();
        Book book = Book.builder().build();

        //when
        when(bookService.findByIsbn(BOOK_ISBN)).thenReturn(Optional.of(book));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.addReview(expectedReviewDto, BOOK_ISBN, user.getUuid()));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenDeleteReviewBookIsbnNotExist() {
        //given
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8wq9ae";
        String reviewUuid = "15e45e7d-4r34-43df-9377-91c63a8cc9ae";
        User user = User.builder().build();

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.deleteReview(bookIsbn, user.getUuid(), reviewUuid));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenDeleteReviewUserNotExist() {
        //given
        String reviewUuid = "15e45e7d-4r34-43df-9377-91c63a8cc9ae";
        User user = User.builder().build();
        Book book = Book.builder().build();

        //when
        when(bookService.findByIsbn(BOOK_ISBN)).thenReturn(Optional.of(book));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.deleteReview(BOOK_ISBN, user.getUuid(), reviewUuid));

        //then
        Assertions.assertEquals(BOOK_OR_USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturn404WhenGetAllBookIsbnNotExist() {
        //given
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8wq9ae";

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                reviewFacade.getAll(bookIsbn));

        //then
        Assertions.assertEquals(BOOK_DOES_NOT_EXIST, thrown.getMessage());
    }
}

