package com.nagarro.advanced.framework.persistence.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private static final String REGEX_USERNAME = "^[A-Za-z\\d._]{3,15}$";
    private static final String INVALID_USERNAME =
            "The username can contain only letters, numbers and special characters";

    private final String uuid;

    @NotBlank(message = "Password cannot be null or empty")
    private final String password;

    @NotNull(message = "username cannot be null")
    @Pattern(regexp = REGEX_USERNAME, message = INVALID_USERNAME)
    private final String username;
    private final ArrayList<SimpleGrantedAuthority> authorities;

    public CustomUserDetails(String uuid, String password, String username, Role role) {
        this.uuid = uuid;
        this.password = password;
        this.username = username;
        this.authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public ArrayList<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
