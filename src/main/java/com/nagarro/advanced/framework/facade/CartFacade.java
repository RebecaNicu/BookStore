package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.controller.model.CartDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Cart;
import com.nagarro.advanced.framework.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartFacade {
    private final CartService cartService;
    private final Converter<Cart, CartDto> cartConverter;

    @Autowired
    public CartFacade(CartService cartService, Converter<Cart, CartDto> cartConverter) {
        this.cartService = cartService;
        this.cartConverter = cartConverter;
    }

    public CartDto addBookToCart(String userUuid, String productUuid) {
        Cart cart = cartService.addBookToCart(userUuid, productUuid);
        return cartConverter.toDto(cart);
    }

    public CartDto deleteBookFromCart(String userUuid, String bookIsbn) {
        Cart cart = cartService.removeBookFromCart(userUuid, bookIsbn);
        return cartConverter.toDto(cart);
    }

    public List<BookDto> getUserCartBooks(String userUuid) {
        return cartConverter.toDto(cartService.findCartByUserUuid(userUuid)).getBooks();
    }

    public CartDto clearUserCart(String userUuid) {
        return cartConverter.toDto(cartService.clearUserCart(userUuid));
    }
}
