package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.BookReviewDto;
import com.nagarro.advanced.framework.facade.BookReviewFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookReviewController {
    private final BookReviewFacade reviewFacade;

    public BookReviewController(BookReviewFacade reviewFacade) {
        this.reviewFacade = reviewFacade;
    }

    @PostMapping("/books/{book_isbn}/users/{user_uuid}/reviews")
    @ResponseBody
    @Secured({"ROLE_USER"})
    public ResponseEntity<BookReviewDto> save(@RequestBody @Validated BookReviewDto review,
                                              @PathVariable("user_uuid") String userUuid,
                                              @PathVariable("book_isbn") String bookIsbn) {
        return new ResponseEntity<>(reviewFacade.addReview(review, bookIsbn, userUuid), HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{book_isbn}/users/{user_uuid}/reviews/{review_uuid}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Void> delete(@PathVariable("book_isbn") String bookIsbn,
                                       @PathVariable("user_uuid") String userUuid,
                                       @PathVariable("review_uuid") String reviewUuid) {
        reviewFacade.deleteReview(bookIsbn, userUuid, reviewUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/books/{book_isbn}/reviews")
    @Secured({"ROLE_USER"})
    public ResponseEntity<List<BookReviewDto>> getAll(@PathVariable("book_isbn") String bookIsbn) {
        return new ResponseEntity<>(reviewFacade.getAll(bookIsbn), HttpStatus.OK);
    }

    @PutMapping("/books/{book_isbn}/users/{user_uuid}/reviews/{review_uuid}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Void> update(@PathVariable("book_isbn") String bookIsbn,
                                       @PathVariable("user_uuid") String userUuid,
                                       @PathVariable("review_uuid") String reviewUuid,
                                       @RequestBody @Validated BookReviewDto reviewDto) {
        reviewFacade.updateReview(bookIsbn, userUuid, reviewUuid, reviewDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
