package com.nagarro.advanced.framework.controller.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegistrationDto {

    private static final String REGEX_USERNAME = "^[A-Za-z\\d._]{3,15}$";
    private static final String INVALID_USERNAME =
            "The username can contain only letters, numbers and special characters";
    private static final String REGEX_FOR_NAME = "^[a-zA-Z]((?! {2})[a-zA-z ]){2,}$";
    private static final String INVALID_NAME = "Name can not be null and can contain only letters and spaces";
    private static final String INVALID_ADDRESS = "The address can contain only letters, spaces, commas and digits and must have at least 2 characters";
    private static final String REGEX_ADDRESS = "^[A-Za-z\\d ,]{2,144}$";
    private static final String INVALID_PHONE_NUMBER =
            "The phone number can have the prefixes: +40 or 00 or just 0. Just romanian numbers only";
    private static final String REGEX_ROMANIAN_PHONE = "^([+][4][0]|[0][0]|[0])[1-9][0-9]{8}$";

    @NotNull(message = "username cannot be null")
    @Pattern(regexp = REGEX_USERNAME, message = INVALID_USERNAME)
    private String username;

    @NotBlank(message = "Password cannot be null or empty")
    private String password;

    @NotBlank(message = "Password cannot be null or empty")
    private String matchingPassword;

    @Email(message = "Email should be valid")
    @NotBlank(message = "email cannot be null or empty")
    private String email;

    @NotNull(message = "firstName cannot be null")
    @Pattern(regexp = REGEX_FOR_NAME, message = INVALID_NAME)
    private String firstName;

    @NotNull(message = "lastName cannot be null")
    @Pattern(regexp = REGEX_FOR_NAME, message = INVALID_NAME)
    private String lastName;

    @NotNull(message = "address cannot be null")
    @Pattern(regexp = REGEX_ADDRESS, message = INVALID_ADDRESS)
    private String address;

    @NotNull(message = "phoneNumber cannot be null")
    @Pattern(regexp = REGEX_ROMANIAN_PHONE, message = INVALID_PHONE_NUMBER)
    private String phoneNumber;

    @Pattern(regexp = "^(ADMIN|USER|)$", message = "Invalid role name")
    @NotNull(message = "role name cannot be null")
    private String roleName;
}
