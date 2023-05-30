package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper implements Converter<User, UserRegistrationDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserRegistrationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public User toEntity(UserRegistrationDto registrationUserDto) {
        return modelMapper.map(registrationUserDto, User.class);
    }

    @Override
    public UserRegistrationDto toDto(User user) {
        return modelMapper.map(user, UserRegistrationDto.class);
    }
}
