package com.nagarro.advanced.framework.service;

import com.nagarro.advanced.framework.controller.model.UserRegistrationDto;
import com.nagarro.advanced.framework.exception.AppException;
import com.nagarro.advanced.framework.persistence.entity.Cart;
import com.nagarro.advanced.framework.persistence.entity.Role;
import com.nagarro.advanced.framework.persistence.entity.User;
import com.nagarro.advanced.framework.persistence.repository.CartRepository;
import com.nagarro.advanced.framework.persistence.repository.RoleRepository;
import com.nagarro.advanced.framework.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String ROLE_DOESN_T_EXIST = " Role not found";
    private static final String USER_DOESN_T_EXIST = " User not found";
    private static final String DUPLICATE_DATA = " Some of the user details already exist in the database";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    public User saveUser(User user) {
        if (areUserEmailAndPhoneUnique(user)) {
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            return userRepository.save(user);
        } else {
            throw new AppException(DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<User> findUserByUuid(String uuid) {
        return userRepository.findUserByUuid(uuid);
    }

    public void deleteUserByUuid(String uuid) {
        Optional<Cart> userCart = cartRepository.findByUserUuid(uuid);
        userCart.ifPresent(cartRepository::delete);
        findUserByUuid(uuid).ifPresent(userRepository::delete);
    }

    @Transactional
    public void updateUserByUuid(String uuid, User user) {
        Optional<User> currentUser = findUserByUuid(uuid);
        if (currentUser.isPresent()) {
            if (areUserEmailAndPhoneUnique(user)) {
                currentUser.get().setUsername(user.getUsername());
                Optional<Role> optionalRetrievedRole = roleRepository.findRoleByName(user.getRole().getName());
                optionalRetrievedRole.ifPresent(currentUser.get()::setRole);
                currentUser.get().setPassword(user.getPassword());
                currentUser.get().setEmail(user.getEmail());
                currentUser.get().setAddress(user.getAddress());
                currentUser.get().setLastName(user.getLastName());
                currentUser.get().setFirstName(user.getFirstName());
                currentUser.get().setPhoneNumber(user.getPhoneNumber());
                userRepository.save(currentUser.get());
            } else {
                throw new AppException(DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new AppException(USER_DOESN_T_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByRole(String roleName) {
        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new AppException(roleName + ROLE_DOESN_T_EXIST, HttpStatus.NOT_FOUND));
        return userRepository.findAllByRole(role);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User register(UserRegistrationDto registrationUserDto, User user) {
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        Role role = roleRepository.findRoleByName(registrationUserDto.getRoleName())
                .orElseThrow(() -> new AppException(ROLE_DOESN_T_EXIST, HttpStatus.NOT_FOUND));
        user.setRole(role);
        return saveUser(user);
    }

    public boolean checkNewUserDataIsUnique(UserRegistrationDto registrationUserDto) {
        String username = registrationUserDto.getUsername();
        String email = registrationUserDto.getEmail();
        String phoneNumber = registrationUserDto.getPhoneNumber();
        Optional<User> userDuplicate = userRepository.findByUsernameOrEmailOrPhoneNumber(username, email, phoneNumber);
        if (userDuplicate.isPresent()) {
            throw new AppException(DUPLICATE_DATA, HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    private boolean areUserEmailAndPhoneUnique(User searchedUser) {
        List<User> users = findAll();
        for (User user : users) {
            if (searchedUser.getEmail().equals(user.getEmail()) ||
                    searchedUser.getPhoneNumber().equals(user.getPhoneNumber())) {
                return false;
            }
        }
        return true;
    }
}
