package com.nagarro.advanced.framework.controller.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    @NotBlank(message = "User uuid cannot be null or empty")
    private String userUuid;

    @NotBlank(message = "List of books cannot be null or empty")
    private List<BookDto> books = new ArrayList<>();
}
