package com.nagarro.advanced.framework.controller.model;

import com.nagarro.advanced.framework.persistence.entity.Role;

public class UserRegistration {

    private final String uuid;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String phoneNumber;
    private final String role;

    public UserRegistration(String uuid, String username, String email, String firstName, String lastName, String address,
                            String phoneNumber, Role role) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role.getName();
    }
}
