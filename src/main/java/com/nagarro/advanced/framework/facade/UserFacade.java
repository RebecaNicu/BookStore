package com.nagarro.advanced.framework.facade;

import com.nagarro.advanced.framework.controller.model.UserDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.service.RoleService;
import com.nagarro.advanced.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFacade {

    private final Converter<User, UserDto> userMapper;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserFacade(Converter<User, UserDto> userMapper, UserService userService, RoleService roleService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.roleService = roleService;
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        Role role = roleService.findRoleByName(user.getRole().getName());
        user.setRole(role);
        try {
            User currentUser = userService.saveUser(user);
            return userMapper.toDto(currentUser);
        } catch (RuntimeException e) {
            throw new AppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public UserDto findUserByUuid(String uuid) {
        return userMapper.toDto(userService.findUserByUuid(uuid).orElseThrow());
    }

    public void deleteUserByUuid(String uuid) {
        userService.deleteUserByUuid(uuid);
    }

    public void updateUserByUuid(String uuid, UserDto userDto) {
        userService.updateUserByUuid(uuid, userMapper.toEntity(userDto));
    }

    public List<UserDto> findAll() {
        return userService.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> findAllByRole(String roleName) {
        return userService.findAllByRole(roleName)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

}
