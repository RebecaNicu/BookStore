package com.nagarro.advanced.framework.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private static final String REGEX_FOR_NAME = "^[a-zA-Z]((?! {2})[a-zA-z ]){2,}$";
    private static final String INVALID_NAME = "Name can contain only letters and spaces";

    private String uuid;

    @NotNull(message = "name cannot be null")
    @Pattern(regexp = REGEX_FOR_NAME, message = INVALID_NAME)
    private String name;
}
