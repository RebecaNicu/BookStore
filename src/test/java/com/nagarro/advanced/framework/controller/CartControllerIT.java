package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.BookDto;
import com.nagarro.advanced.framework.controller.model.CartDto;
import com.nagarro.advanced.framework.persistence.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:test-data/insert_cart_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CartControllerIT {

    private static final String CART_URL = "/carts/users/";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mock;

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void addBookToCartShouldReturnCartForValidInput() throws Exception {
        //given
        String bookIsbn = "33e45e7d-3e34-43df-9366-91c66a8cc9ae";
        User user = User.builder().uuid("89e45e7d-3e34-43df-9366-91c66a8cc9ff").build();
        List<BookDto> books = List.of(BookDto.builder().build());
        CartDto cartDto = CartDto.builder().userUuid(user.getUuid()).build();
        cartDto.setBooks(books);

        //then
        mock.perform(put(CART_URL + user.getUuid() + "/books/" + bookIsbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userUuid").exists())
                .andExpect(jsonPath("books").isNotEmpty());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void addBookToCartShouldResponseWithStatus400WhenUserNotExists() throws Exception {
        //given
        String bookIsbn = "33e45e7d-3e34-43df-9366-91c66a8cc9ae";

        //then
        mock.perform(put(CART_URL + "5770c971-b29a-4a7c-96c4-cb47e513a234" + "/books/" + bookIsbn))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void addBookToCartShouldResponseWith404WhenBookNotExists() throws Exception {
        //given
        User user = User.builder().uuid("89e45e7d-3e34-43df-9366-91c66a8cc9ff").build();

        //then
        mock.perform(put(CART_URL + user.getUuid() + "/books529eb087-9a76-4842-b562-945ee8c372a8/"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn204WhenDeleteBookFromCartForValidInput() throws Exception {
        //given
        String userUuid = "89e45e7d-3e34-43df-9366-91c66a8cc9ff";
        String bookIsbn = "33e45e7d-3e34-43df-9366-91c66a8cc9ae";

        //then
        mock.perform(delete(CART_URL + userUuid + "/books/" + bookIsbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("userUuid").exists())
                .andExpect(jsonPath("books").isEmpty());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldRespondWith404WhenDeleteBookFromCartBookNotExists() throws Exception {
        //given
        String userUuid = "89e45e7d-3e34-43df-9366-91c66a8cc9ff";

        //then
        mock.perform(delete(CART_URL + userUuid + "/books/92e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn200WhenGetUserCartBooksForValidInput() throws Exception {
        //given
        String userUuid = "89e45e7d-3e34-43df-9366-91c66a8cc9ff";

        //then
        mock.perform(get(CART_URL + userUuid + "/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldRespondWith400WhenGetUserCartBooksUserNotExists() throws Exception {
        //given
        String userUuid = "32e45e7d-3e34-43df-9366-91c66a8cc9ff";

        //then
        mock.perform(get(CART_URL + userUuid + "/books"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn204WhenClearUserCartForValidInput() throws Exception {
        //given
        String userUuid = "89e45e7d-3e34-43df-9366-91c66a8cc9ff";

        //then
        mock.perform(delete(CART_URL + userUuid + "/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldRespondWith400WhenClearUserCartUserNotExists() throws Exception {
        //given
        String userUuid = "39e45e7d-3e34-43df-9366-91c66a8cc9ff";

        //then
        mock.perform(delete(CART_URL + userUuid + "/books"))
                .andExpect(status().isBadRequest());
    }
}