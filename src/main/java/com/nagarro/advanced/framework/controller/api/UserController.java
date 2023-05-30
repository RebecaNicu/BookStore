package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.UserDto;
import com.nagarro.advanced.framework.facade.UserFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    public UserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Validated UserDto userDto) {
        return new ResponseEntity<>(userFacade.saveUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> findUserByUuid(@PathVariable String uuid) {
        return new ResponseEntity<>(userFacade.findUserByUuid(uuid), HttpStatus.OK);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> findAllUsers(@RequestParam(required = false) String roleName) {
        List<UserDto> allUsers;
        if (roleName == null || roleName.isEmpty()) {
            allUsers = userFacade.findAll();
        } else {
            roleName = roleName.toUpperCase();
            allUsers = userFacade.findAllByRole(roleName);
        }
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> deleteUserByUuid(@PathVariable String uuid) {
        userFacade.deleteUserByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> updateUserByUuid(@PathVariable String uuid, @RequestBody @Validated UserDto userDto) {
        userFacade.updateUserByUuid(uuid, userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}