package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private static final String DUPLICATE_DATA = "Duplicate entry for role name!";
    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService(roleRepository);
    }

    @Test
    void saveRoleShouldReturnRoleForValidInput() {
        //given
        long id = 1;
        Role expectedRole = new Role();
        expectedRole.setId(id);
        expectedRole.setName("admin");

        //when
        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);
        Role actualRole = roleService.saveRole(expectedRole);

        //then
        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getName(), actualRole.getName());
    }

    @Test
    void saveRoleShouldRespondWith400ForDuplicateRoleName() {
        //given
        long id = 1;
        Role role = new Role();
        role.setId(id);
        role.setName("admin");

        //when
        when(roleRepository.findAll()).thenReturn(List.of(role));
        AppException appException = assertThrows(AppException.class, ()
                -> roleService.saveRole(role));

        assertEquals(DUPLICATE_DATA, appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void findRoleByIdShouldReturnRoleForValidId() {
        //given
        long id = 1;
        Role expectedRole = new Role();
        expectedRole.setId(id);
        expectedRole.setName("admin");

        //when
        when(roleRepository.findById(id)).thenReturn(Optional.of(expectedRole));
        Role actualRole = roleService.findRoleById(id);

        //then
        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getName(), actualRole.getName());
    }

    @Test
    void findRoleByIdShouldThrowExceptionForInvalidId() {
        //given
        long id = 1;

        //when
        when(roleRepository.findById(id)).thenThrow(AppException.class);

        //then
        assertThrows(AppException.class, () -> roleService.findRoleById(id));
    }

    @Test
    void findRoleByNameShouldReturnRoleForValidName() {
        //given
        String name = "admin";
        long id = 1;
        Role expectedRole = new Role();
        expectedRole.setId(id);
        expectedRole.setName(name);

        //when
        when(roleRepository.findRoleByName(name)).thenReturn(Optional.of(expectedRole));
        Role actualRole = roleService.findRoleByName(name);

        //then
        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getName(), actualRole.getName());
    }

    @Test
    void findRoleByNameShouldThrowExceptionForInvalidName() {
        //given
        String name = "1";

        //when
        when(roleRepository.findRoleByName(name)).thenThrow(AppException.class);

        //then
        assertThrows(AppException.class, () -> roleService.findRoleByName(name));
    }

    @Test
    void updateRoleByIdShouldUpdateRoleForValidInput() {
        //given
        Role expectedRole = new Role();
        String name = "admin";
        long id = 1;
        expectedRole.setId(id);
        expectedRole.setName(name);

        //when
        when(roleRepository.findById(id)).thenReturn(Optional.of(expectedRole));
        Role actualRole = new Role();
        actualRole.setName("Customer");
        roleService.updateRoleById(id, actualRole);

        //then
        assertEquals(expectedRole.getName(), actualRole.getName());
    }

    @Test
    void deleteRoleByIdShouldDeleteRoleForValidInput() {
        //given
        Role expectedRole = new Role();
        String name = "admin";
        long id = 1;
        expectedRole.setId(id);
        expectedRole.setName(name);

        //when
        when(roleRepository.findById(id)).thenReturn(Optional.of(expectedRole));
        roleService.deleteRoleById(id);

        //then
        verify(roleRepository).delete(expectedRole);
    }

    @Test
    void findAllShouldReturnListOfAllRoles() {
        //given
        Role expectedRole1 = new Role();
        expectedRole1.setId(1L);
        expectedRole1.setName("admin");
        Role expectedRole2 = new Role();
        expectedRole2.setId(2L);
        expectedRole2.setName("user");
        List<Role> expectedList = new ArrayList<>();
        expectedList.add(expectedRole1);
        expectedList.add(expectedRole2);

        //when
        when(roleRepository.findAll()).thenReturn(expectedList);
        List<Role> actualList = roleService.findAll();

        //then
        assertEquals(expectedList, actualList);
    }
}
