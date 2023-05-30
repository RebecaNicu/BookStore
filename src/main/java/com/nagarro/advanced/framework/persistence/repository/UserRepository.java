package com.nagarro.advanced.framework.persistence.repository;

import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUuid(String uuid);

    List<User> findAll();

    List<User> findAllByRole(Role role);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmailOrPhoneNumber(String username, String email, String phoneNumber);

    Optional<User> findUserByUsername(String username);
}

