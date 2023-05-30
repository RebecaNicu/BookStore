package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.LoginUser;
import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.controller.model.UserRegistrationResult;
import com.nagarro.advanced.framework.facade.AuthenticationFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public AuthController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@Valid @RequestBody LoginUser user) {
        return new ResponseEntity<>(authenticationFacade.login(user), HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegistrationResult> register(@Valid @RequestBody UserRegistrationDto registrationUserDto) {
        UserRegistrationResult userRegistration = authenticationFacade.register(registrationUserDto);
        return new ResponseEntity<>(userRegistration, HttpStatus.CREATED);
    }
}
