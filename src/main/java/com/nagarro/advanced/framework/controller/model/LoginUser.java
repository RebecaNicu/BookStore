package com.nagarro.advanced.framework.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginUser {

    private static final String REGEX_USERNAME = "^[A-Za-z\\d._]{3,15}$";
    private static final String INVALID_USERNAME =
            "The username can contain only letters, numbers and special characters";

    @NotNull(message = "username cannot be null")
    @Pattern(regexp = REGEX_USERNAME, message = INVALID_USERNAME)
    private String username;

    @NotNull(message = "password cannot be null")
    private String password;
}
