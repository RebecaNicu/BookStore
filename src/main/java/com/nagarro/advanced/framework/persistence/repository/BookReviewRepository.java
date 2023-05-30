package com.nagarro.advanced.framework.persistence.repository;

import com.nagarro.advanced.framework.persistence.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    Optional<BookReview> findByUuid(String uuid);
    List<BookReview> findAllByBookIsbn(String bookIsbn);
}

