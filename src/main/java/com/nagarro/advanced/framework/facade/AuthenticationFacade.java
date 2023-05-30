package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.LoginUser;
import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.controller.model.UserRegistrationResult;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.service.UserService;
import com.nagarro.advanced.framework.util.UserRegistrationResultCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFacade {

    private static final String MATCHING_PASSWORD_ERROR = "Passwords doesn't match";
    public static final String NON_MATCHING_PASSWORDS = "Passwords doesn't match!";
    public static final String BAD_CREDENTIALS = "No account associate with this username!";
    private final UserService userService;
    private final Converter<User, UserRegistrationDto> userRegistrationMapper;
    private final UserRegistrationResultCreator userCreator;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthenticationFacade(UserService userService, Converter<User, UserRegistrationDto> userRegistrationMapper, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.userRegistrationMapper = userRegistrationMapper;
        this.encoder = encoder;
        this.userCreator = new UserRegistrationResultCreator();
    }

    public UserRegistrationResult register(UserRegistrationDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getMatchingPassword())) {
            throw new BadCredentialsException(MATCHING_PASSWORD_ERROR);
        }
        if (userService.checkNewUserDataIsUnique(registrationUserDto)) {
            User user = userRegistrationMapper.toEntity(registrationUserDto);
            User userResult = userService.register(registrationUserDto, user);
            return userCreator.createRegistrationResult(userResult);
        } else {
            throw new AppException("Some of users details already exists!", HttpStatus.BAD_REQUEST);
        }
    }

    public String login(LoginUser user) {
        Optional<User> optionalUser = userService.findUserByUsername(user.getUsername());
        if (optionalUser.isPresent()) {
            if (encoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
                return "Login successfully!";
            } else {
                throw new AppException(NON_MATCHING_PASSWORDS, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new AppException(BAD_CREDENTIALS, HttpStatus.NOT_FOUND);
        }
    }
}
