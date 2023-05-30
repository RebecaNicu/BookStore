package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.facade.BookFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookFacade bookFacade;

    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @Operation(summary = "Save book", description = "Save book into library", tags = "Save")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created!",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Book already exists!",
                    content = @Content)})
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BookDto> save(@RequestBody @Validated BookDto book) {
        return new ResponseEntity<>(bookFacade.saveBook(book), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a book", description = "Get a book from library", tags = "Get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book!",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found!",
                    content = @Content)})
    @GetMapping("/{isbn}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        return new ResponseEntity<>(bookFacade.findBookByIsbn(isbn), HttpStatus.OK);
    }

    @Operation(summary = "Delete book", description = "Delete book from library", tags = "Delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete successfully!"),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    @DeleteMapping("/{isbn}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteByIsbn(@PathVariable("isbn") String isbn) {
        bookFacade.deleteBookByIsbn(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update book", description = "Update book from library", tags = "Update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Update successfully!"),
            @ApiResponse(responseCode = "404", description = "Book not found")})
    @PutMapping("/{isbn}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> update(@RequestBody @Validated BookDto book, @PathVariable("isbn") String isbn) {
        bookFacade.updateBook(isbn, book);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
