package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.persistence.entity.BookReview;
import com.nagarro.advanced.framework.persistence.repository.BookReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReviewServiceTest {

    @InjectMocks
    private BookReviewService reviewService;
    @Mock
    private BookReviewRepository reviewRepository;

    @Test
    void shouldReturnReviewWhenAddReview() {
        //given
        BookReview expectedReview = BookReview.builder().uuid("15e45e7d-4r34-43bn-9377-91c63a8cc9ae").id(3L).build();

        //when
        when(reviewRepository.save(expectedReview)).thenReturn(expectedReview);
        BookReview actualReview = reviewService.addReview(expectedReview);

        //then
        assertEquals(expectedReview, actualReview);
    }

    @Test
    void shouldReturnDeletedReviewWhenDeleteReview() {
        //given
        BookReview expectedReview = BookReview.builder().uuid("15e45e7d-4r34-43df-9377-91c63a8cc9ae").id(1L).build();

        String reviewUuid = expectedReview.getUuid();

        //when
        when(reviewRepository.findByUuid(reviewUuid)).thenReturn(Optional.of(expectedReview));
        reviewService.deleteByUuid(reviewUuid);

        //then
        verify(reviewRepository).delete(expectedReview);
    }

    @Test
    void shouldReturnReviewWhenFindByUuid() {
        //given
        BookReview review = BookReview.builder().uuid("15e45e7d-4r34-43df-9377-91c63a8cc9ae").id(1L).build();

        String reviewUuid = review.getUuid();
        Optional<BookReview> expectedReview = Optional.of(review);

        //when
        when(reviewRepository.findByUuid(reviewUuid)).thenReturn(expectedReview);
        Optional<BookReview> actualReview = reviewService.findByUuid(reviewUuid);

        //then
        assertEquals(expectedReview, actualReview);
    }

    @Test
    void shouldReturnReviewsWhenFindAllReviews() {
        //given
        String bookIsbn = "33e45e7d-3e34-43df-9366-91c66a8cc9ae";
        List<BookReview> expectedReviews = List.of(BookReview.builder().build());

        //when
        when(reviewRepository.findAllByBookIsbn(bookIsbn))
                .thenReturn(expectedReviews);
        List<BookReview> actualReviews = reviewService.findAll(bookIsbn);

        //then
        assertEquals(expectedReviews, actualReviews);
    }

    @Test
    void shouldReturnUpdatedReviewWhenUpdate() {
        //given
        BookReview expectedReview = BookReview.builder().uuid("15e45e7d-4r34-43df-9377-91c63a8cc9ae").id(1L).build();
        String reviewUuid = expectedReview.getUuid();

        //when
        when(reviewRepository.findByUuid(reviewUuid)).thenReturn(Optional.of(expectedReview));
        reviewService.updateReview(reviewUuid, expectedReview);

        //then
        verify(reviewRepository).save(expectedReview);
    }
}
