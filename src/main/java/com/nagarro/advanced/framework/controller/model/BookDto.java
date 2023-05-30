package com.nagarro.advanced.framework.controller.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private static final String REGEX_DETAILS = "^[A-Za-z\\d !\"#$%&'()*+,-./:;<=>?@^_`{|}~]{0,144}$";
    private static final String REGEX_FOR_TITLE = "^[A-Za-z][A-Za-z ]{0,144}$";
    private static final String REGEX_FOR_AUTHOR = "^[A-Za-z][A-Za-z ]{0,144}$";
    private static final String REQUIRED_CATEGORY_UUID = "Category Uuid is required";
    private static final String INVALID_PRICE = "Price cannot be negative or zero!";
    private static final String INVALID_TITLE = "Title can not be null and can contain only letters and spaces";
    private static final String INVALID_AUTHOR = "Author can not be null and can contain only letters and spaces";
    private static final String INVALID_DETAILS = "The details can contain only letters, spaces, punctuation marks and digits";

    private String isbn;

    @NotBlank(message = REQUIRED_CATEGORY_UUID)
    private String categoryUuid;

    @Positive(message = INVALID_PRICE)
    private BigDecimal price;

    @Pattern(regexp = REGEX_FOR_TITLE, message = INVALID_TITLE)
    private String title;

    @Pattern(regexp = REGEX_FOR_AUTHOR, message = INVALID_AUTHOR)
    private String author;

    @Pattern(regexp = REGEX_DETAILS, message = INVALID_DETAILS)
    private String details;
}
