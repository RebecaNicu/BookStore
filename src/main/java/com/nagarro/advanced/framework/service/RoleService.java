package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveRole(Role role) {
        if (isRoleNameUnique(role)) {
            return roleRepository.save(role);
        } else {
            throw new AppException("Duplicate entry for role name!", HttpStatus.BAD_REQUEST);
        }
    }

    public Role findRoleById(long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new AppException("Could not find role with that id", HttpStatus.NOT_FOUND));
    }

    public Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new AppException("Could not find role with that name", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public void updateRoleById(long id, Role role) {
        Role currentRole = findRoleById(id);
        currentRole.setName(role.getName());
        roleRepository.save(currentRole);
    }

    public void deleteRoleById(long id) {
        Role currentRole = findRoleById(id);
        roleRepository.delete(currentRole);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    private boolean isRoleNameUnique(Role searchedRole) {
        List<Role> roles = findAll();
        for (Role role : roles) {
            if (role.getName().equals(searchedRole.getName())) {
                return false;
            }
        }
        return true;
    }
}
