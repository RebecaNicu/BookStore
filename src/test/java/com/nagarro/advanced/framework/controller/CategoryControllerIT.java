package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.CategoryDto;
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
@Sql(value = "classpath:test-data/insert_category_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryControllerIT {

    private static final String URL = "/categories";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldSaveCategoryAndRespondWith201() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("adventure");

        //then
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(jsonPath("name").value(categoryDto.getName()))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void shouldSaveCategoryAndRespondWith403() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("adventure");

        //then
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldSaveCategoryAndRespondWith400WhenSaveDuplicateCategory() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("comedy");

        //then
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldFindCategoryByUuidAndRespondWith200() throws Exception {
        mockMvc.perform(get(URL + "/94b23f0a-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(jsonPath("name").value("thriller"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldFindCategoryByUuidAndRespondWith404WhenCategoryNotFound() throws Exception {
        mockMvc.perform(get(URL + "/24b23f0a-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldFindCategoryByNameAndRespondWith200() throws Exception {
        mockMvc.perform(get(URL + "?name=comedy"))
                .andExpect(jsonPath("uuid").value("29e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", password = "ADMINPASS", roles = "USER")
    @Test
    void shouldFindCategoryByNameAndRespondWith404WhenCategoryNameNotFound() throws Exception {
        mockMvc.perform(get(URL + "?name=romance"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldDeleteCategoryByUuidAndRespondWith204() throws Exception {
        mockMvc.perform(delete(URL + "/29e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void shouldDeleteCategoryByUuidAndRespondWith403() throws Exception {
        mockMvc.perform(delete(URL + "/29e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldDeleteCategoryByUuidAndRespondWith404WhenCategoryNotFound() throws Exception {
        mockMvc.perform(delete(URL + "/09e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldUpdateCategoryByUuidAndRespondWith200() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("love");

        //then
        this.mockMvc.perform(put(URL + "/29e45e7d-3e34-43df-9366-91c66a8cc9ae")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void shouldUpdateCategoryByUuidAndRespondWith403() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("love");

        //then
        this.mockMvc.perform(put(URL + "/29e45e7d-3e34-43df-9366-91c66a8cc9ae")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldUpdateCategoryByUuidAndRespondWith400WhenCategoryNotFound() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("love");

        //then
        this.mockMvc.perform(put(URL + "/19e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void shouldUpdateCategoryByUuidAndRespondWith400WhenCategoryNameAlreadyExists() throws Exception {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("comedy");

        //then
        this.mockMvc.perform(put(URL + "/29e45e7d-3e34-43df-9366-91c66a8cc9ae"))
                .andExpect(status().isBadRequest());
    }
}
