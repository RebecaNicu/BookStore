package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.persistence.entity.CustomUserDetails;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User nor found!");
        }

        return new CustomUserDetails(user.get().getUuid(), user.get().getPassword(), user.get().getUsername(), user.get().getRole());
    }
}
