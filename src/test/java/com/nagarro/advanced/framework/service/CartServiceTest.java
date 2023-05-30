package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Book;
import com.nagarro.advanced.framework.persistence.entity.Cart;
import com.nagarro.advanced.framework.persistence.entity.Category;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.persistence.repository.BookRepository;
import com.nagarro.advanced.framework.persistence.repository.CartRepository;
import com.nagarro.advanced.framework.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.will;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final String BOOK_DOES_NOT_EXIST =
            "The book with isbn: 25e45e7d-3e34-43df-9366-91c66a8cc9ae doesn't exist";
    private static final String CART_DOES_NOT_EXIST =
            "The cart with id: 95e45e7d-3e34-43df-9366-91c66a8cc9ae doesn't exist";
    private static final String USER_DOES_NOT_EXIST = "User doesn't exists!";
    @InjectMocks
    private CartService cartService;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnCartWhenAddBookToCartWithValidInput() {
        //given
        List<Book> books = getBooks();
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8cc9ae";
        User user = new User();
        user.setUuid("75e45e7d-3e34-43df-9366-91c66a8cc9ae");
        Cart expectedCart = Cart.builder().id(1L).user(user).books(books).build();
        String userUuid = expectedCart.getUser().getUuid();

        //when
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.of(expectedCart.getBooks().get(0)));
        when(cartRepository.findByUserUuid(userUuid)).thenReturn(Optional.of(expectedCart));
        when(userRepository.findUserByUuid(userUuid)).thenReturn(Optional.of(user));
        Cart actualCart = cartService.addBookToCart(userUuid, bookIsbn);

        //then
        assertEquals(expectedCart, actualCart);
    }

    @Test
    void shouldReturnCartWhenClearUserCart() {
        //given
        User user = new User();
        user.setUuid("75e45e7d-3e34-43df-9366-91c66a8cc9ae");
        List<Book> books = getBooks();
        Cart expectedCart =  Cart.builder().id(1L).user(user).books(books).build();
        String userUuid = expectedCart.getUser().getUuid();

        //when
        when(cartRepository.findByUserUuid(userUuid)).thenReturn(Optional.of(expectedCart));
        when(userRepository.findUserByUuid(userUuid)).thenReturn(Optional.of(user));
        Cart actualCart = cartService.clearUserCart(userUuid);

        //then
        assertEquals(expectedCart, actualCart);
    }

    @Test
    void shouldReturnAppExceptionWhenAddBookToCartBookNotExists() {
        //given
        String userUuid = "95e45e7d-3e34-43df-9366-91c66a8cc9ae";
        String bookIsbn = "25e45e7d-3e34-43df-9366-91c66a8cc9ae";
        User user = new User();
        user.setUuid("25e45e7d-3e34-43df-9366-91c66a8cc9ae");

        //when
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.empty());
        when(userRepository.findUserByUuid(userUuid)).thenReturn(Optional.of(user));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.addBookToCart(userUuid, bookIsbn));

        //then
        Assertions.assertEquals(BOOK_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturnAppExceptionWhenAdBookToCartUserNotExists() {
        //given
        String bookIsbn = "25e45e7d-3e34-43df-9366-91c66a8cc9ae";
        User user = new User();
        user.setUuid("25e45e7d-3e34-43df-9366-91c66a8cc9ae");

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.addBookToCart(user.getUuid(), bookIsbn));

        //then
        Assertions.assertEquals(USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturnCartWhenRemoveBookFromCart() {
        //given
        String bookIsbn = "15e45e7d-3e34-43df-9366-91c66a8cc9ae";
        Category category = new Category();
        category.setName("c1");
        Book mockBook = Book.builder().isbn("15e45e7d-3e34-43df-9366-91c66a8cc9ae").title("dxs").author("dxc ds")
                .details("details").price(BigDecimal.valueOf(99.9)).category(category).build();
        User user = new User();
        user.setUuid("75e45e7d-3e34-43df-9366-91c66a8cc9ae");
        Book book = Book.builder().isbn("75e45e7d-3e34-43df-9366-91c66a8cc9ae").title("Ion").author("Liviu Rebreanu")
                .details("details").price(BigDecimal.valueOf(99.9)).category(category).build();
        Cart expectedCart = Cart.builder().id(1L).user(user).books(List.of(book)).build();
        String userUuid = expectedCart.getUser().getUuid();

        //when
        when(cartRepository.findByUserUuid(userUuid)).thenReturn(Optional.of(expectedCart));
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.of(mockBook));
        when(userRepository.findUserByUuid(userUuid)).thenReturn(Optional.of(user));
        Cart actualCart = cartService.removeBookFromCart(userUuid, bookIsbn);

        //then
        assertEquals(expectedCart.getBooks(), actualCart.getBooks());
    }

    @Test
    void shouldReturnAppExceptionWhenRemoveBookFromCartNonexistentCart() {
        //given
        User user = new User();
        user.setUuid("95e45e7d-3e34-43df-9366-91c66a8cc9ae");
        Category category = new Category();
        category.setName("c1");
        String bookIsbn = "25e45e7d-3e34-43df-9366-91c66a8cc9ae";
        Book mockBook = Book.builder().isbn("25e45e7d-3e34-43df-9366-91c66a8cc9ae").title("dxs").author("dxc ds")
                .details("details").price(BigDecimal.valueOf(99.9)).category(category).build();

        //when
        when(cartRepository.findByUserUuid(user.getUuid())).thenReturn(Optional.empty());
        when(userRepository.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(bookRepository.findByIsbn(bookIsbn)).thenReturn(Optional.of(mockBook));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.removeBookFromCart(user.getUuid(), bookIsbn));

        //then
        assertEquals(CART_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturnAppExceptionWhenClearUserCart() {
        //given
        User user = User.builder().uuid("95e45e7d-3e34-43df-9366-91c66a8cc9ae").build();

        //when
        when(cartRepository.findByUserUuid(user.getUuid())).thenReturn(Optional.empty());
        when(userRepository.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.clearUserCart(user.getUuid()));

        //then
        Assertions.assertEquals(CART_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturnCartWhenFindCartByUserUuid() {
        //given
        User user = User.builder().uuid("75e45e7d-3e34-43df-9366-91c66a8cc9ae").build();
        Cart expectedCart = Cart.builder().id(1L).user(user).build();
        String userUuid = expectedCart.getUser().getUuid();

        //when
        when(cartRepository.findByUserUuid(userUuid)).thenReturn(Optional.of(expectedCart));
        when(userRepository.findUserByUuid(userUuid)).thenReturn(Optional.of(user));
        Cart actualCart = cartService.findCartByUserUuid(userUuid);

        //then
        assertEquals(expectedCart, actualCart);
    }

    @Test
    void shouldReturnAppExceptionWhenFindCartByUserUuidCartNotFound() {
        //given
        User user = new User();
        user.setUuid("95e45e7d-3e34-43df-9366-91c66a8cc9ae");

        //when
        when(userRepository.findUserByUuid(user.getUuid())).thenReturn(Optional.of(user));
        when(cartRepository.findByUserUuid(user.getUuid())).thenReturn(Optional.empty());
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.findCartByUserUuid(user.getUuid()));

        //then
        Assertions.assertEquals(CART_DOES_NOT_EXIST, thrown.getMessage());
    }

    @Test
    void shouldReturnAppExceptionWhenFindCartByUserUuidUserNotFound() {
        //given
        User user = new User();
        user.setUuid("95e45e7d-3e34-43df-9366-91c66a8cc9ae");

        //when
        AppException thrown = Assertions.assertThrows(AppException.class, () ->
                cartService.findCartByUserUuid(user.getUuid()));

        //then
        Assertions.assertEquals(USER_DOES_NOT_EXIST, thrown.getMessage());
    }

    private List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("15e45e7d-3e34-43df-9366-91c66a8cc9ae");
        books.add(book);
        return books;

    }
}
