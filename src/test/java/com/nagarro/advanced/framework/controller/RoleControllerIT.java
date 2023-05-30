package com.nagarro.advanced.framework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.advanced.framework.controller.model.RoleDto;
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
@Sql(value = "classpath:test-data/insert_role_test_data.sql")
@Sql(value = "classpath:test-data/truncate-all-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RoleControllerIT {

    private final static String ROLE_URL = "/roles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveRoleShouldSaveRoleInDBAndRespondWithStatus201ForValidRole() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");

        //then
        mockMvc.perform(post(ROLE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(roleDto.getName()));
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void saveRoleShouldSaveRoleInDBAndRespondWithStatus403ForUnauthorizedUser() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");

        //then
        mockMvc.perform(post(ROLE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveRoleShouldRespondWithStatus400ForDuplicateRoleName() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("admin");

        //then
        mockMvc.perform(post(ROLE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void saveRoleShouldSaveRoleInDBAndRespondWithStatus400WhenAddNewAdmin() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("ADMIN");

        //then
        mockMvc.perform(post(ROLE_URL))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findRoleByIdShouldRespondWithStatus200AndReturnRoleForValidId() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("admin"));
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void findRoleByIdShouldRespondWithStatus403UnauthorizedUser() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL + "/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findRoleByIdShouldRespondWithStatus404WhenIdNotExists() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL + "/{id}", 5))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findRoleByNameShouldReturnStatus200AndRoleForValidName() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL).param("name", "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value("user"));
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void findRoleByNameShouldReturnStatus403UnauthorizedUser() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL).param("name", "user"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findRoleByNameShouldReturnStatus404WhenRoleNameNotExists() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL).param("name", "srfs"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void findAllRolesShouldRespondWithStatus200AndReturnListOfRoles() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("admin"))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].name").value("user"));
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void findAllRolesShouldRespondWithStatus403UnauthorizedUser() throws Exception {
        //then
        mockMvc.perform(get(ROLE_URL))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void deleteRoleByIdShouldRespondWithStatus204() throws Exception {
        //then
        mockMvc.perform(delete(ROLE_URL + "/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void deleteRoleByIdShouldRespondWithStatus403() throws Exception {
        //then
        mockMvc.perform(delete(ROLE_URL + "/{id}", 1))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void deleteRoleByIdShouldRespondWithStatus404WhenIdNotExists() throws Exception {
        //then
        mockMvc.perform(delete(ROLE_URL + "/{id}", 5))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateRoleByIdShouldRespondWithStatus200() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");

        //then
        mockMvc.perform(put(ROLE_URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "USER", password = "USERPASS", roles = "USER")
    @Test
    void updateRoleByIdShouldRespondWithStatus403() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");

        //then
        mockMvc.perform(put(ROLE_URL + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "ADMIN", password = "ADMINPASS", roles = "ADMIN")
    @Test
    void updateRoleByIdShouldRespondWithStatus400WhenIdNotExists() throws Exception {
        //given
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");

        //then
        mockMvc.perform(put(ROLE_URL + "/{id}", 5))
                .andExpect(status().isBadRequest());
    }
}
