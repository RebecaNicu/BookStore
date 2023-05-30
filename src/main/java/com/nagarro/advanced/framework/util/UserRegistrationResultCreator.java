package com.nagarro.advanced.framework.util;

import com.nagarro.advanced.framework.controller.model.UserRegistrationResult;
import com.nagarro.advanced.framework.persistence.entity.User;

public class UserRegistrationResultCreator {

    public UserRegistrationResult createRegistrationResult(User user) {
        return new UserRegistrationResult(user.getUuid(), user.getUsername(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getAddress(), user.getPhoneNumber(), user.getRole().getName());
    }
}
