package com.nagarro.advanced.framework.util;

public enum RoleType {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    public String authority;

    RoleType(String value) {
        authority = value;
    }
}
