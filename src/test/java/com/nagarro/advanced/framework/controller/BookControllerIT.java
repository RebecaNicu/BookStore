package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:test-data/insert_book_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerIT {

    private static final String BASE_URL = "/books";
    private static final String ISBN_FIELD = "isbn";
    private static final String CATEGORY_UUID_FIELD = "categoryUuid";
    private static final String PRICE_FIELD = "price";
    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";
    private static final String DETAILS_FIELD = "details";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveShouldSaveBookToDBForValidInputAndRespondWithStatus201() throws Exception {
        //given
        BookDto bookDto = new BookDto("33e45e7d-3e34-43df-9366-91c66a8cc9ff",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(40.5), "Book", "Author", "details");

        //then
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(jsonPath(ISBN_FIELD).exists())
                .andExpect(jsonPath(CATEGORY_UUID_FIELD).value(bookDto.getCategoryUuid()))
                .andExpect(jsonPath(PRICE_FIELD).value(bookDto.getPrice()))
                .andExpect(jsonPath(TITLE_FIELD).value(bookDto.getTitle()))
                .andExpect(jsonPath(AUTHOR_FIELD).value(bookDto.getAuthor()))
                .andExpect(jsonPath(DETAILS_FIELD).value(bookDto.getDetails())).andExpect(status().isCreated());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void saveShouldRespondWithStatus403() throws Exception {
        //given
        BookDto bookDto = new BookDto("33e45e7d-3e34-43df-9366-91c66a8cc9ff",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(40.5), "Book", "Author", "details");

        //then
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveShouldRespondWithStatus400WhenSaveExistentTitleAndAuthorBok() throws Exception {
        //given
        BookDto bookDto = new BookDto("19e45e7d-3e34-43df-9366-91c66a8cc9ae",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(25), "Poezii", "Eminescu", "sdsees");
        //then
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void findByIsbnShouldReturnValidBookForValidIsbnAndRespondWithStatus200() throws Exception {
        //given
        BookDto expectedProductDto = new BookDto("99e45e7d-3e34-43df-9366-91c66a8cc9ae",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(15.0), "Poezii", "Eminescu", "details");

        //then
        this.mockMvc.perform(get(BASE_URL + "/{isbn}", expectedProductDto.getIsbn()))
                .andExpect(jsonPath(CATEGORY_UUID_FIELD).value(expectedProductDto.getCategoryUuid()))
                .andExpect(jsonPath(PRICE_FIELD).value(expectedProductDto.getPrice()))
                .andExpect(jsonPath(TITLE_FIELD).value(expectedProductDto.getTitle()))
                .andExpect(jsonPath(AUTHOR_FIELD).value(expectedProductDto.getAuthor()))
                .andExpect(jsonPath(DETAILS_FIELD).value(expectedProductDto.getDetails())).andExpect(status().isOk());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void findByIsbnShouldRespondWithStatus404() throws Exception {
        //given
        BookDto expectedProductDto = new BookDto("49e45e7d-3e34-43df-9366-91c66a8cc9ae",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(15.0), "Poezii", "Eminescu", "details");

        //then
        this.mockMvc.perform(get(BASE_URL + "/{isbn}", expectedProductDto.getIsbn()))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void deleteByIsbnShouldDeleteBookWihValidIsbnFromDBAndRespondWithStatus204() throws Exception {
        //then
        this.mockMvc.perform(delete(BASE_URL + "/{isbn}", "47e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void deleteByIsbnShouldRespondWithStatus403() throws Exception {
        //then
        this.mockMvc.perform(delete(BASE_URL + "/{isbn}", "47e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void deleteByIsbnShouldRespondWithStatus404() throws Exception {
        //then
        this.mockMvc.perform(delete(BASE_URL + "/{isbn}", "77e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateShouldUpdateProductForValidInputAndRespondWithStatus204() throws Exception {
        //given
        BookDto newBookDto = new BookDto("99e45e7d-3e34-43df-9366-91c66a8cc9ae",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(20), "Izvor", "Eminovici", "detaails");

        //then
        this.mockMvc.perform(put(BASE_URL + "/{isbn}", "93e45e7d-3e34-43df-9366-91c66a8cc9ae")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDto)))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void updateShouldRespondWithStatus403() throws Exception {
        //given
        BookDto newBookDto = new BookDto("99e45e7d-3e34-43df-9366-91c66a8cc9ae",
                "29e45e7d-3e34-43df-9366-91c66a8cc9ae", BigDecimal.valueOf(20), "Izvor", "Eminovici", "detaails");

        //then
        this.mockMvc.perform(put(BASE_URL + "/{isbn}", "93e45e7d-3e34-43df-9366-91c66a8cc9ae")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDto)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateShouldRespondWithStatus400() throws Exception {
        //then
        this.mockMvc.perform(put(BASE_URL + "/{isbn}", "13e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isBadRequest());
    }
}
