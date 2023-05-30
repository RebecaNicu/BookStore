package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Cart;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.persistence.repository.BookRepository;
import com.nagarro.advanced.framework.persistence.repository.CartRepository;
import com.nagarro.advanced.framework.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private static final String DO_NOT_EXIST_MESSAGE = " doesn't exist";
    private static final String USER_DOESN_T_EXISTS = "User doesn't exists!";
    public static final String BOOK_DOES_NOT_EXISTS = "The book doesn't exists!";
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart addBookToCart(String userUuid, String bookIsbn) {
        Optional<User> user = userRepository.findUserByUuid(userUuid);
        if (user.isPresent()) {
            Optional<Cart> optionalCart = cartRepository.findByUserUuid(userUuid);
            Cart customerCart = optionalCart.orElseGet(() -> new Cart(user.get()));
            Optional<Book> optionalBook = bookRepository.findByIsbn(bookIsbn);
            Book book = optionalBook.orElseThrow(() -> new AppException("The book with isbn: " + bookIsbn + DO_NOT_EXIST_MESSAGE, HttpStatus.NOT_FOUND));
            customerCart.getBooks().add(book);
            return customerCart;
        } else {
            throw new AppException(USER_DOESN_T_EXISTS, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Cart removeBookFromCart(String userUuid, String bookIsbn) {
        Optional<Book> optionalBook = bookRepository.findByIsbn(bookIsbn);
        if (optionalBook.isPresent()) {
            Cart customerCart = this.findCartByUserUuid(userUuid);
            this.deleteBook(customerCart, bookIsbn);
            return customerCart;
        } else {
            throw new AppException(BOOK_DOES_NOT_EXISTS, HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public Cart clearUserCart(String userUuid) {
        Cart customerCart = this.findCartByUserUuid(userUuid);
        customerCart.getBooks().clear();
        return customerCart;
    }

    public Cart findCartByUserUuid(String uuid) {
        Optional<User> user = userRepository.findUserByUuid(uuid);
        if (user.isPresent()) {
            Optional<Cart> optionalCart = cartRepository.findByUserUuid(uuid);
            return optionalCart.orElseThrow(() -> new AppException("The cart with id: " + uuid + DO_NOT_EXIST_MESSAGE,
                    HttpStatus.NOT_FOUND));
        } else {
            throw new AppException(USER_DOESN_T_EXISTS, HttpStatus.BAD_REQUEST);
        }
    }

    private void deleteBook(Cart cart, String bookIsbn) {
        List<Book> books = cart.getBooks();
        for (Book book : books) {
            if (book.getIsbn().equals(bookIsbn)) {
                books.remove(book);
                break;
            }
        }
    }
}