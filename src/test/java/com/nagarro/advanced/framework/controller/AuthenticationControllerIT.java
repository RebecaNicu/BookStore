package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.LoginUser;
import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:test-data/insert_user_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthenticationControllerIT {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturn201WhenRegisterUserForValidInput() throws Exception {
        //given
        UserRegistrationDto userRegistration = UserRegistrationDto.builder().username("iancu").email("iancu@gmail.com")
                .firstName("marcel").lastName("iancu").phoneNumber("0732458987").address("fvds")
                .password("123").matchingPassword("123").roleName("ADMIN").build();

        //then
        mock.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistration))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("uuid").exists())
                .andExpect(jsonPath("username").value("iancu"))
                .andExpect(jsonPath("email").value("iancu@gmail.com"))
                .andExpect(jsonPath("firstName").value("marcel"))
                .andExpect(jsonPath("lastName").value("iancu"))
                .andExpect(jsonPath("phoneNumber").value("0732458987"))
                .andExpect(jsonPath("address").value("fvds"))
                .andExpect(jsonPath("role").value("ADMIN"));
    }

    @Test
    void shouldReturn401WhenRegisterUserNotMatchingPassword() throws Exception {
        //given
        UserRegistrationDto userRegistration = UserRegistrationDto.builder().username("iancu").email("iancu@gmail.com")
                .firstName("marcel").lastName("iancu").phoneNumber("0732458987").address("fvds")
                .password("123").matchingPassword("13").roleName("ADMIN").build();

        //then
        mock.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistration))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401WhenRegisterUserDuplicateUsername() throws Exception {
        //given
        UserRegistrationDto userRegistration = UserRegistrationDto.builder().username("pauna123").email("iancu@gmail.com")
                .firstName("marcel").lastName("iancu").phoneNumber("0732458987").address("fvds")
                .password("123").matchingPassword("13").roleName("ADMIN").build();

        //then
        mock.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistration))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRespondWith200WhenLoginUserValidInput() throws Exception {
        LoginUser loginUser = LoginUser.builder().username("iliescu").password("passcad").build();

        mock.perform(get("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUser))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void shouldRespondWithStatusCode400WhenLoginUserNonMatchingPasswords() throws Exception {
        LoginUser loginUser = LoginUser.builder().username("iliescu").password("password").build();

        mock.perform(get("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUser))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithStatus404WhenLoginUserInvalidUsername() throws Exception {
        LoginUser loginUser = LoginUser.builder().username("iescu").password("passcad").build();

        mock.perform(get("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUser))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}
