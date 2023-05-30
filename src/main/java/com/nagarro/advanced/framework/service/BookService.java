package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.persistence.repository.BookRepository;
import com.nagarro.advanced.framework.persistence.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookService {

    private static final String NONEXISTENT_CATEGORY = "The category doesn't exist!";
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteByIsbn(String isbn) {
        this.findByIsbn(isbn).ifPresent(bookRepository::delete);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional
    public void update(String isbn, Book newBook) {
        Optional<Book> book = findByIsbn(isbn);
        if (book.isPresent()) {
            book.get().setPrice(newBook.getPrice());
            book.get().setDetails(newBook.getDetails());
            Optional<Category> optionalCategory = categoryRepository.findByUuid(newBook.getCategory().getUuid());
            if (optionalCategory.isPresent()) {
                optionalCategory.get().addBook(book.get());
            } else {
                throw new AppException(NONEXISTENT_CATEGORY, HttpStatus.NOT_FOUND);
            }
            this.save(book.get());
        }
    }

    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public Optional<Book> findByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }
}
