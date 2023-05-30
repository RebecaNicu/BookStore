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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookReviewFacade {

    private static final String BOOK_OR_USER_DOES_NOT_EXIST = "There is no book or user with this id!";
    private static final String BOOK_DOES_NOT_EXIST = "There is no book with this id!";
    public static final String REVIEW_DOES_NOT_EXIST = "There is no review with this uuid";
    private final BookReviewService reviewService;
    private final BookService bookService;
    private final UserService userService;
    private final BookReviewMapper reviewMapper;

    @Autowired
    public BookReviewFacade(BookReviewService reviewService, BookService bookService, UserService userService, BookReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.bookService = bookService;
        this.userService = userService;
        this.reviewMapper = reviewMapper;
    }

    public BookReviewDto addReview(BookReviewDto review, String bookIsbn, String userUuid) {
        Optional<Book> optionalBook = bookService.findByIsbn(bookIsbn);
        Optional<User> optionalUser = userService.findUserByUuid(userUuid);

        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            BookReview bookReview = reviewMapper.toEntity(review);
            book.addReview(bookReview);
            User user = optionalUser.get();
            user.addReview(bookReview);
            return reviewMapper.toDto(reviewService.addReview(bookReview));
        } else {
            throw new AppException(BOOK_OR_USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteReview(String bookIsbn, String userUuid, String reviewUuid) {
        Optional<Book> optionalBook = bookService.findByIsbn(bookIsbn);
        Optional<User> optionalUser = userService.findUserByUuid(userUuid);
        Optional<BookReview> optionalReview = reviewService.findByUuid(reviewUuid);
        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            if (optionalReview.isPresent()) {
                reviewService.deleteByUuid(reviewUuid);
            } else {
                throw new AppException(REVIEW_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
            }
        } else {
            throw new AppException(BOOK_OR_USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    public List<BookReviewDto> getAll(String bookIsbn) {
        Optional<Book> optionalProduct = bookService.findByIsbn(bookIsbn);
        if (optionalProduct.isPresent()) {
            List<BookReview> reviews = reviewService.findAll(bookIsbn);
            return reviewMapper.convertToReviewList(reviews);
        } else {
            throw new AppException(BOOK_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    public void updateReview(String bookIsbn, String userUuid, String reviewUuid, BookReviewDto reviewDto) {
        Optional<Book> optionalBook = bookService.findByIsbn(bookIsbn);
        Optional<User> optionalUser = userService.findUserByUuid(userUuid);
        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            reviewService.updateReview(reviewUuid, reviewMapper.toEntity(reviewDto));
        } else {
            throw new AppException(BOOK_OR_USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }
}