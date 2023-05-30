package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.persistence.entity.BookReview;
import com.nagarro.advanced.framework.persistence.repository.BookReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {

    private final BookReviewRepository reviewRepository;

    @Autowired
    public BookReviewService(BookReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public BookReview addReview(BookReview review) {
        return reviewRepository.save(review);
    }

    public void deleteByUuid(String uuid) {
        reviewRepository.findByUuid(uuid).ifPresent(reviewRepository::delete);
    }

    public Optional<BookReview> findByUuid(String uuid) {
        return reviewRepository.findByUuid(uuid);
    }

    @Transactional
    public void updateReview(String reviewUuid, BookReview newReview) {
        Optional<BookReview> review = findByUuid(reviewUuid);
        if (review.isPresent()) {
            review.get().setTitle(newReview.getTitle());
            review.get().setBody(newReview.getBody());
            review.get().setStarValue(newReview.getStarValue());
            addReview(review.get());
        }
    }

    public List<BookReview> findAll(String bookIsbn) {
        return reviewRepository.findAllByBookIsbn(bookIsbn);
    }
}
