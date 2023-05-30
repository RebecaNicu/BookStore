package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.RoleDto;
import com.nagarro.advanced.framework.controller.model.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = "classpath:test-data/insert_user_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerIT {

    private static final String USERS_URL = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findAllUsersShouldRespondWithStatus200ForValidInputWithoutRoleName() throws Exception {
        mockMvc.perform(get(USERS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findAllUsersShouldRespondWithStatus200ForValidInputWithRoleName() throws Exception {
        mockMvc.perform(get(USERS_URL + "?name=admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findUserByUuidShouldRespondWithStatus200ForValidUuid() throws Exception {
        //given
        String uuid = "9770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(get(USERS_URL + "/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("radu@gmail.com"))
                .andExpect(jsonPath("username").value("mihaescu"))
                .andExpect(jsonPath("firstName").value("radu"))
                .andExpect(jsonPath("lastName").value("pauna"))
                .andExpect(jsonPath("address").value("Cluj"))
                .andExpect(jsonPath("phoneNumber").value("0773458967"));
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveUserShouldRespondWithStatus201ForValidInput() throws Exception {
        //given
        RoleDto role = new RoleDto("ADMIN");
        UserDto user = UserDto.builder().uuid(UUID.randomUUID().toString()).email("Johnny@yahoo.com")
                .username("Johnny").firstName("Johnny").lastName("Johnny").address("address")
                .phoneNumber("0712345678").password("password").role(role).build();

        //then
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("uuid").exists())
                .andExpect(jsonPath("email").value("Johnny@yahoo.com"))
                .andExpect(jsonPath("username").value("Johnny"))
                .andExpect(jsonPath("firstName").value("Johnny"))
                .andExpect(jsonPath("lastName").value("Johnny"))
                .andExpect(jsonPath("address").value("address"))
                .andExpect(jsonPath("phoneNumber").value("0712345678"))
                .andExpect(jsonPath("password").value("password"))
                .andExpect(jsonPath("role").exists());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveUserShouldRespondWithStatus400ForDuplicateUserDetails() throws Exception {
        RoleDto role = new RoleDto("ADMIN");
        UserDto user = UserDto.builder().uuid(UUID.randomUUID().toString()).email("vlad@gmail.com")
                .username("csni").firstName("Johnny").lastName("Johnny").address("address")
                .phoneNumber("0712345678").password("password").role(role).build();

        //then
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void saveUserShouldRespondWithStatus403ForUnauthorizedUser() throws Exception {
        RoleDto role = new RoleDto("admin");
        UserDto user = UserDto.builder().uuid(UUID.randomUUID().toString()).email("vlad@gmail.com")
                .username("csni").firstName("Johnny").lastName("Johnny").address("address")
                .phoneNumber("0712345678").password("password").role(role).build();

        //then
        mockMvc.perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveUserShouldRespondWithStatus400UsernameAlreadyExists() throws Exception {
        //then
        mockMvc.perform(post(USERS_URL))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void deleteUserByUuidShouldReturnStatus200ForValidUuid() throws Exception {
        //given
        String uuid = "5770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(delete(USERS_URL + "/{uuid}", uuid)).andExpect(status().isOk());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void deleteUserByUuidShouldReturnStatus403ForUnauthorizedUser() throws Exception {
        //given
        String uuid = "5770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(delete(USERS_URL + "/{uuid}", uuid)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateUserByUuidShouldReturnStatus200ForValidUuid() throws Exception {
        //given
        RoleDto role = new RoleDto("admin");
        UserDto updatedUser = UserDto.builder().username("mihgescu").email("rdu@gmail.com").firstName("dfvnel")
                .lastName("pnuna").address("Cluj").phoneNumber("0713458967").password("passcad").role(role).build();
        String uuid = "9770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(put(USERS_URL + "/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateUserByUuidShouldReturnStatus400ForDuplicateDetails() throws Exception {
        //given
        RoleDto role = new RoleDto("admin");
        UserDto updatedUser = UserDto.builder().username("mihgescu").email("radu@gmail.com").firstName("dfvnel")
                .lastName("pnuna").address("Cluj").phoneNumber("0713458967").password("passcad").role(role).build();
        String uuid = "9770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(put(USERS_URL + "/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateUserByUuidShouldReturnStatus400UserNotExists() throws Exception {
        //given
        String uuid = "4770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(put(USERS_URL + "/{uuid}", uuid))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void updateUserByUuidShouldReturnStatus403ForUnauthorizedUser() throws Exception {
        //given
        RoleDto role = new RoleDto("admin");
        UserDto updatedUser = UserDto.builder().username("mihgescu").email("rdu@gmail.com").firstName("dfvnel")
                .lastName("pnuna").address("Cluj").phoneNumber("0713458967").password("passcad").role(role).build();
        String uuid = "9770c971-b29a-4a7c-96c4-cb47e513a234";

        //then
        mockMvc.perform(put(USERS_URL + "/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}