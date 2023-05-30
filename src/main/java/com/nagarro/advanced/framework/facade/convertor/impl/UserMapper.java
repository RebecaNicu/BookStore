package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.UserDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements Converter<User, UserDto> {
    private final ModelMapper modelMapper;

    public User toEntity(UserDto object) {
        return modelMapper.map(object, User.class);
    }

    public UserDto toDto(User object) {
        return modelMapper.map(object, UserDto.class);
    }
}
