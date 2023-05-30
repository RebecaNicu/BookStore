package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.RoleDto;
import com.nagarro.advanced.framework.facade.RoleFacade;
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
@RequestMapping("/roles")
public class RoleController {

    private final RoleFacade roleFacade;

    public RoleController(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RoleDto> saveRole(@RequestBody @Validated RoleDto roleDto) {
        return new ResponseEntity<>(roleFacade.saveRole(roleDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RoleDto> findRoleById(@PathVariable long id) {
        return new ResponseEntity<>(roleFacade.findRoleById(id), HttpStatus.OK);
    }

    @GetMapping(params = "name")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RoleDto> findRoleByName(@RequestParam String name) {
        return new ResponseEntity<>(roleFacade.findRoleByName(name), HttpStatus.OK);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<RoleDto>> findAllRoles() {
        return new ResponseEntity<>(roleFacade.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RoleDto> deleteRoleById(@PathVariable long id) {
        roleFacade.deleteRoleById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<RoleDto> updateRoleById(@PathVariable long id, @RequestBody @Validated RoleDto roleDto) {
        roleFacade.updateRoleById(id, roleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
