package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.controller.model.CartDto;
import com.nagarro.advanced.framework.facade.CartFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartFacade cartFacade;

    @Autowired
    public CartController(CartFacade cartFacade) {
        this.cartFacade = cartFacade;
    }

    @PutMapping("/users/{userUuid}/books/{bookIsbn}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<CartDto> addBookToCart(@PathVariable("userUuid") String userUuid,
                                                 @PathVariable("bookIsbn") String bookIsbn) {
        return new ResponseEntity<>(cartFacade.addBookToCart(userUuid, bookIsbn), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userUuid}/books/{bookIsbn}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<CartDto> deleteBookFromCart(@PathVariable("userUuid") String userUuid,
                                                      @PathVariable("bookIsbn") String bookIsbn) {
        return new ResponseEntity<>(cartFacade.deleteBookFromCart(userUuid, bookIsbn), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userUuid}/books")
    @Secured({"ROLE_USER"})
    public ResponseEntity<List<BookDto>> getUserCartBooks(@PathVariable("userUuid") String userUuid) {
        return new ResponseEntity<>(cartFacade.getUserCartBooks(userUuid), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userUuid}/books")
    @Secured({"ROLE_USER"})
    public ResponseEntity<CartDto> clearUserCart(@PathVariable("userUuid") String userUuid) {
        return new ResponseEntity<>(cartFacade.clearUserCart(userUuid), HttpStatus.NO_CONTENT);
    }
}
