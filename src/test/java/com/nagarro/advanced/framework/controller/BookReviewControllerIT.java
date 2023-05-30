package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.BookReviewDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:test-data/insert_review_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookReviewControllerIT {

    private static final String BASE_URL = "/books/33e45e7d-3e34-43df-9366-91c66a8cc9ae" +
            "/users/89e45e7d-3e34-43df-9366-91c66a8cc9ff/reviews";
    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn201WhenAddReviewForValidInput() throws Exception {
        //given
        BookReviewDto reviewDto = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63v8cc9ae").title("hihi")
                .body("like").starValue(3).build();

        //then
        mock.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("uuid").exists())
                .andExpect(jsonPath("title").value("hihi"))
                .andExpect(jsonPath("body").value("like"))
                .andExpect(jsonPath("starValue").value(3));
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn400WhenAddReviewNonExistentBook() throws Exception {
        //then
        mock.perform(post("/books/13e45e7d-3e34-43df-9366-91c66a8cc9ae" +
                        "/users/89e45e7d-3e34-43df-9366-91c66a8cc9ff/reviews"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn204WhenDeleteReviewForValidInput() throws Exception {
        //then
        mock.perform(delete(BASE_URL + "/66e45e7d-3e34-43df-9366-91c66a8cc9mm"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn404WhenDeleteReviewNonExistentBook() throws Exception {
        //then
        mock.perform(delete("/books/23e45e7d-3e34-43df-9366-91c66a8cc9ae" +
                        "/users/89e45e7d-3e34-43df-9366-91c66a8cc9ff/reviews/36e45e7d-3e34-43df-9366-91c66a8cc9gm"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn404WhenDeleteReviewNonExistentReview() throws Exception {
        //then
        mock.perform(delete(BASE_URL + "/36g45e7d-3e34-43ef-9366-91c66a8cc9gm"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn200WhenGetAllReviewsForValidInput() throws Exception {
        //then
        mock.perform(get("/books/33e45e7d-3e34-43df-9366-91c66a8cc9ae/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").exists())
                .andExpect(jsonPath("$[0].title").value("excellent"))
                .andExpect(jsonPath("$[0].body").value("magic"))
                .andExpect(jsonPath("$[0].starValue").value(5))
                .andExpect(jsonPath("$[1].uuid").exists())
                .andExpect(jsonPath("$[1].title").value("bad"))
                .andExpect(jsonPath("$[1].body").value("i don t like it"))
                .andExpect(jsonPath("$[1].starValue").value(2));
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn404WhenGetAllReviewsNonExistentBook() throws Exception {
        //then
        mock.perform(get("/books/73e45e7d-3e34-43df-9366-91c66a8cc9ae/reviews"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldReturn200WhenUpdateReview() throws Exception {
        //given
        BookReviewDto review = BookReviewDto.builder().uuid("15e45e7d-4r34-43df-9366-91c63v8cc9ae").title("hihi")
                .body("like").starValue(5).build();

        //then
        mock.perform(put(BASE_URL + "/89e45e7d-3e34-43df-9366-91c66a8cc9mm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldRespondWithStatus400WhenUpdateReviewNonExistentBook() throws Exception {
        //then
        mock.perform(put("/books/83e45e7d-3e34-43df-9366-91c66a8cc9ae" +
                        "/users/89e45e7d-3e34-43df-9366-91c66a8cc9ff/reviews" + "/89e45e7d-3e34-43df-9366-91c66a8cc9mm"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldRespondWithStatus400WhenUpdateReviewNonExistentReview() throws Exception {
        //then
        mock.perform(put("/books/83e45e7d-3e34-43df-9366-91c66a8cc9ae" +
                        "/users/89e45e7d-3e34-43df-9366-91c66a8cc9ff/reviews" + "/29e45e7d-3e34-43df-9366-91c66a8cc9mm"))
                .andExpect(status().isBadRequest());
    }
}
