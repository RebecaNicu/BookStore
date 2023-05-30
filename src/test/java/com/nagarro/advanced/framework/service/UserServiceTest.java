package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.persistence.repository.RoleRepository;
import com.nagarro.advanced.framework.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String DUPLICATE_DATA = " Some of the user details already exist in the database";
    private static final String ROLE_DOESN_T_EXIST = " Role not found";
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void saveUserShouldSaveForValidUser() {
        //given
        User expectedUser = User.builder().build();
        Role role = new Role();
        role.setName("admin");
        expectedUser.setRole(role);

        //when
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        User actualUser = userService.saveUser(expectedUser);

        //then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void saveUserShouldRespondWith400ForDuplicateUserDetails() {
        //given
        User user = User.builder().email("margareta@gmail.com").build();
        Role role = new Role();
        role.setName("user");
        user.setRole(role);

        //when
        when(userService.findAll()).thenReturn(List.of(user));
        AppException appException = assertThrows(AppException.class, ()
                -> userService.saveUser(user));

        assertEquals(DUPLICATE_DATA, appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void findUsersByUuidShouldReturnUserForValidUuid() {
        //given
        User expectedUser = User.builder().username("john").phoneNumber("0712345678").build();
        String uuid = UUID.randomUUID().toString();
        expectedUser.setUuid(uuid);
        Role role = new Role();
        role.setName("admin");
        expectedUser.setRole(role);

        //when
        when(userRepository.findUserByUuid(uuid)).thenReturn(Optional.of(expectedUser));
        Optional<User> actualUser = userService.findUserByUuid(uuid);

        //then
        assertEquals(Optional.of(expectedUser), actualUser);
    }

    @Test
    void findUsersByUuidShouldThrowExceptionForInvalidUuid() {
        //given
        User expectedUser = User.builder().username("john").phoneNumber("0712345678").build();
        String uuid = "invalidUuid";
        expectedUser.setUuid(uuid);

        //when
        when(userRepository.findUserByUuid(uuid)).thenThrow(AppException.class);

        //then
        assertThrows(AppException.class, () -> userService.findUserByUuid(uuid));
    }

    @Test
    void deleteUserByUuidShouldDeleteUserForValidUuid() {
        //given
        User expectedUser = User.builder().username("john").phoneNumber("0712345678").build();
        String uuid = UUID.randomUUID().toString();
        expectedUser.setUuid(uuid);

        //when
        when(userRepository.findUserByUuid(uuid)).thenReturn(Optional.of(expectedUser));
        userService.deleteUserByUuid(uuid);

        //then
        verify(userRepository).delete(expectedUser);
    }

    @Test
    void updateUserByUuidShouldUpdateUserForValidUuidAndUserInput() {
        //given
        User expectedUser = User.builder().username("john").phoneNumber("0712345678").build();
        User actualUser = User.builder().username("johnathan").phoneNumber("0712345678").build();
        String uuid = UUID.randomUUID().toString();
        expectedUser.setUuid(uuid);
        Role role = new Role();
        role.setName("admin");
        actualUser.setRole(role);
        expectedUser.setRole(role);

        //when
        when(userRepository.findUserByUuid(uuid)).thenReturn(Optional.of(expectedUser));
        when(roleRepository.findRoleByName(expectedUser.getRole().getName()))
                .thenReturn(Optional.ofNullable(expectedUser.getRole()));
        userService.updateUserByUuid(uuid, actualUser);

        //then
        assertEquals(expectedUser.getAddress(), actualUser.getAddress());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getRole().getName(), actualUser.getRole().getName());
    }

    @Test
    void updateUserShouldRespondWith400ForDuplicateUserDetails() {
        //given
        User user = User.builder().uuid("15e45e7d-3e34-43df-9366-91c66a8cc9ae").email("margareta@gmail.com").build();
        Role role = new Role();
        role.setName("user");
        user.setRole(role);

        //when
        when(userRepository.findUserByUuid("15e45e7d-3e34-43df-9366-91c66a8cc9ae")).thenReturn(Optional.of(user));
        when(userService.findAll()).thenReturn(List.of(user));
        AppException appException = assertThrows(AppException.class, ()
                -> userService.updateUserByUuid("15e45e7d-3e34-43df-9366-91c66a8cc9ae", user));

        assertEquals(DUPLICATE_DATA, appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void findAllShouldReturnListOfUsersForValidInput() {
        //given
        List<User> expectedList = new ArrayList<>();
        User userJohn = User.builder().username("john").phoneNumber("0712345678").build();
        User userJohny = User.builder().username("johny").phoneNumber("0722345678").build();
        expectedList.add(userJohn);
        expectedList.add(userJohny);

        //when
        when(userRepository.findAll()).thenReturn(expectedList);
        List<User> actualList = userService.findAll();

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void findAllByRoleShouldReturnListOfUsersForValidInput() {
        //given
        String roleName = "ADMIN";
        Role role = new Role();
        role.setName(roleName);
        List<User> expectedList = new ArrayList<>();
        User userJohn = User.builder().username("john").phoneNumber("0712345678").build();
        User userJohny = User.builder().username("johny").phoneNumber("0722345678").build();
        expectedList.add(userJohn);
        expectedList.add(userJohny);

        //when
        when(roleRepository.findRoleByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.findAllByRole(role)).thenReturn(expectedList);
        List<User> actualList = userService.findAllByRole(roleName);

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void findAllByRoleShouldThrowExceptionForInvalidRoleName() {
        //given
        String roleName = "ASDASD";
        Role role = new Role();
        role.setName(roleName);

        //when
        when(roleRepository.findRoleByName(roleName)).thenThrow(AppException.class);

        //then
        assertThrows(AppException.class, () -> userService.findAllByRole(roleName));
    }

    @Test
    void registerUserShouldSaveUserInDbForRoleName() {
        //given
        Role role = new Role();
        role.setName("USER");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").roleName(role.getName()).build();
        User user = User.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role).build();

        //when
        when(roleRepository.findRoleByName(userRegistrationDto.getRoleName())).thenReturn(Optional.of(role));
        when(userService.saveUser(user)).thenReturn(user);
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn(anyString());
        User actualUser = userService.register(userRegistrationDto, user);

        //then
        assertEquals(user.getAddress(), actualUser.getAddress());
        assertEquals(user.getEmail(), actualUser.getEmail());
        assertEquals(user.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user.getFirstName(), actualUser.getFirstName());
        assertEquals(user.getLastName(), actualUser.getLastName());
        assertEquals(user.getRole().getName(), actualUser.getRole().getName());
    }

    @Test
    void registerUserShouldRespondWithStatus400ForInvalidRoleName() {
        //given
        Role role = new Role();
        role.setName("vbfh");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").roleName(role.getName()).build();
        User user = User.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role).build();

        //when
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn(anyString());
        AppException appException = assertThrows(AppException.class, ()
                -> userService.register(userRegistrationDto, user));

        //then
        assertEquals(ROLE_DOESN_T_EXIST, appException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, appException.getHttpStatus());
    }

    @Test
    void checkIfNewUserDataIsUniqueShouldReturnTrueForUniqueData() {
        //given
        Role role = new Role();
        role.setName("USER");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("aad@gmail.com")
                .firstName("fsff").lastName("vfrd").phoneNumber("0723458978").roleName(role.getName()).build();

        //when
        when(userRepository.findByUsernameOrEmailOrPhoneNumber(userRegistrationDto.getUsername(),
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPhoneNumber())).thenReturn(Optional.empty());
        boolean response = userService.checkNewUserDataIsUnique(userRegistrationDto);

        //then
        assertTrue(response);
    }

    @Test
    void checkIfNewUserDataIsUniqueShouldResponseWith400ForDuplicateData() {
        //given
        Role role = new Role();
        role.setName("vbfh");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").roleName(role.getName()).build();
        User user = User.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role).build();

        //when
        when(userRepository.findByUsernameOrEmailOrPhoneNumber(userRegistrationDto.getUsername(),
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPhoneNumber())).thenReturn(Optional.of(user));
        AppException appException = assertThrows(AppException.class, ()
                -> userService.checkNewUserDataIsUnique(userRegistrationDto));

        //then
        assertEquals(DUPLICATE_DATA, appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void findUserByUsernameShouldReturnUserForValidInput() {
        Role role = new Role();
        role.setName("USER");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").roleName(role.getName()).build();
        User user = User.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role).build();

        //when
        when(userRepository.findUserByUsername(userRegistrationDto.getUsername())).thenReturn(Optional.of(user));
        Optional<User> expectedUser = userService.findUserByUsername(userRegistrationDto.getUsername());

        //then
        assertEquals(expectedUser, Optional.of(user));
    }
}
