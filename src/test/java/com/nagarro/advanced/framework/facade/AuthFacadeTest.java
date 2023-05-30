package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.LoginUser;
import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.controller.model.UserRegistrationResult;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    private static final String MATCHING_PASSWORD_ERROR = "Passwords doesn't match";
    public static final String DUPLICATE_DATA = "Some of users details already exists!";

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    @Mock
    BCryptPasswordEncoder encoder;

    @Mock
    private Converter<User, UserRegistrationDto> userRegistrationMapper;

    @Test
    void registerUserShouldRegisterUserInDbForValidInput() {
        //given
        Role role = new Role();
        role.setName("vbfh");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").password("124").matchingPassword("124").roleName(role.getName()).build();
        User user = User.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role).build();
        UserRegistrationResult expectedUser = UserRegistrationResult.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").role(role.getName()).build();

        //when
        when(userService.checkNewUserDataIsUnique(userRegistrationDto)).thenReturn(true);
        when(userRegistrationMapper.toEntity(userRegistrationDto)).thenReturn(user);
        when(userService.register(userRegistrationDto, user)).thenReturn(user);
        UserRegistrationResult actualUser = authenticationFacade.register(userRegistrationDto);

        //then
        assertEquals(expectedUser.getAddress(), actualUser.getAddress());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getPhoneNumber(), actualUser.getPhoneNumber());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getRole(), actualUser.getRole());
    }

    @Test
    void registerUserShouldThrowExceptionWhenPasswordNotMatching() {
        //given
        Role role = new Role();
        role.setName("vbfh");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").password("14").matchingPassword("124").roleName(role.getName()).build();

        //when
        BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class, ()
                -> authenticationFacade.register(userRegistrationDto));

        //then
        assertEquals(MATCHING_PASSWORD_ERROR, badCredentialsException.getMessage());
    }

    @Test
    void registerUserShouldRespondWithStatus400WhenDuplicateData() {
        //given
        Role role = new Role();
        role.setName("vbfh");
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().address("fvtr").email("dfd@gmail.com")
                .firstName("fddf").lastName("vfrd").phoneNumber("sxer").password("124").matchingPassword("124").roleName(role.getName()).build();

        //when
        when(userService.checkNewUserDataIsUnique(userRegistrationDto)).thenReturn(false);
        AppException appException = assertThrows(AppException.class, ()
                -> authenticationFacade.register(userRegistrationDto));

        //then
        assertEquals(DUPLICATE_DATA, appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void loginShouldReturnSuccessfullyMessageForValidInput() {
        //given
        LoginUser loginUser = LoginUser.builder().username("gigel").password("password").build();
        User actualUser = User.builder().username("gigel").password("password").build();

        //when
        when(userService.findUserByUsername(loginUser.getUsername())).thenReturn(Optional.of(actualUser));
        when(encoder.matches(actualUser.getPassword(), loginUser.getPassword())).thenReturn(true);
        String loginMessage = authenticationFacade.login(loginUser);

        //then
        assertEquals("Login successfully!", loginMessage);
    }

    @Test
    void loginShouldReturn400ForNonMatchingPasswords() {
        //given
        LoginUser loginUser = LoginUser.builder().username("gigel").password("password").build();
        User actualUser = User.builder().username("gigel").password("pass").build();

        //when
        when(userService.findUserByUsername(loginUser.getUsername())).thenReturn(Optional.of(actualUser));
        AppException appException = assertThrows(AppException.class, ()
                -> authenticationFacade.login(loginUser));

        //then
        assertEquals("Passwords doesn't match!", appException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, appException.getHttpStatus());
    }

    @Test
    void loginShouldReturn400ForInvalidUsername() {
        //given
        LoginUser loginUser = LoginUser.builder().username("gigel").password("password").build();

        //when
        AppException appException = assertThrows(AppException.class, ()
                -> authenticationFacade.login(loginUser));

        //then
        assertEquals("No account associate with this username!", appException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, appException.getHttpStatus());
    }
}
