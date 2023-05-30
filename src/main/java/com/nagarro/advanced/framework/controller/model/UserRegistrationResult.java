package com.nagarro.advanced.framework.controller.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationResult {

    private String uuid;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String role;
}
