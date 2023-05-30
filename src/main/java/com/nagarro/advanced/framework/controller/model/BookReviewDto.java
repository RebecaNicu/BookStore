package com.nagarro.advanced.framework.controller.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookReviewDto {

    private static final String REGEX_FOR_TITLE = "^[A-Za-z][A-Za-z\\d !\"#$%&'()*+,-./:;<=>?@^_`{|}~]{0,144}$";
    private static final String REGEX_FOR_USERNAME = "^[A-Za-z][A-Za-z0-9_]{7,29}$";
    private static final String INVALID_TITLE = "Title can not be null and can contain only letters, spaces, punctuation marks and digits";
    private static final int MAX_STAR_VALUE = 5;
    private static final String REGEX_BODY = "^[A-Za-z\\d !\"#$%&'()*+,-./:;<=>?@^_`{|}~]{0,144}$";
    private static final String INVALID_BODY = "The details can contain only letters, spaces, punctuation marks and digits";
    public static final String INVALID_STAR_VALUE = "Star value cannot be negative";
    private static final String INVALID_USERNAME = "The username is invalid";

    private String uuid;

    @Pattern(regexp = REGEX_FOR_TITLE, message = INVALID_TITLE)
    private String title;

    @Pattern(regexp = REGEX_BODY, message = INVALID_BODY)
    private String body;

    @Positive(message = INVALID_STAR_VALUE)
    @Max(value = MAX_STAR_VALUE)
    private float starValue;
}
