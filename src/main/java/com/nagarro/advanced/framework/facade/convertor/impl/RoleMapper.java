package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.RoleDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleMapper implements Converter<Role, RoleDto> {

    private final ModelMapper modelMapper;

    public Role toEntity(RoleDto object) {
        return modelMapper.map(object, Role.class);
    }

    public RoleDto toDto(Role object) {
        return modelMapper.map(object, RoleDto.class);
    }
}
