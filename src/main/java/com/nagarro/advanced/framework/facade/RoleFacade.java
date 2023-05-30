package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.RoleDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleFacade {

    private final RoleService roleService;
    private final Converter<Role, RoleDto> roleConverter;

    @Autowired
    public RoleFacade(RoleService roleService, Converter<Role, RoleDto> roleConverter) {
        this.roleService = roleService;
        this.roleConverter = roleConverter;
    }

    public RoleDto saveRole(RoleDto roleDto) {
        Role role = roleService.saveRole(roleConverter.toEntity(roleDto));
        return roleConverter.toDto(role);
    }

    public RoleDto findRoleById(long id) {
        return roleConverter.toDto(roleService.findRoleById(id));
    }

    public RoleDto findRoleByName(String name) {
        return roleConverter.toDto(roleService.findRoleByName(name));
    }

    public void updateRoleById(long id, RoleDto roleDto) {
        Role role = roleConverter.toEntity(roleDto);
        roleService.updateRoleById(id, role);
    }

    public void deleteRoleById(long id) {
        roleService.deleteRoleById(id);
    }

    public List<RoleDto> findAll() {
        return roleService
                .findAll()
                .stream()
                .map(roleConverter::toDto)
                .collect(Collectors.toList());
    }
}
