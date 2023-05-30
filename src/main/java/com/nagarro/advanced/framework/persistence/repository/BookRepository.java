package com.nagarro.advanced.framework.persistence.repository;

import com.nagarro.advanced.framework.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    Optional<Book> findByTitle(String title);

    Optional<Book> findByAuthor(String author);
}
