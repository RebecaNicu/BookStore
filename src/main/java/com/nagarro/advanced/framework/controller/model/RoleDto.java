package com.nagarro.advanced.framework.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {

    private static final String INVALID_NAME = "Name can not be null and can contain only letters and spaces";

    private String id;

    @NotBlank
    @Pattern(regexp = "^(ADMIN|USER|)$", message = "Invalid role name")
    private String name;

    public RoleDto(String name) {
        this.name = name;
    }
}
