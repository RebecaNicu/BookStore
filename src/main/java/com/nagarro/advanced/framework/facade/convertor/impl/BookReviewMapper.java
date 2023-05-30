package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.BookReviewDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.BookReview;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookReviewMapper implements Converter<BookReview, BookReviewDto> {

    private final ModelMapper mapper;

    public BookReview toEntity(BookReviewDto object) {
        return mapper.map(object, BookReview.class);
    }

    public BookReviewDto toDto(BookReview object) {
        return mapper.map(object, BookReviewDto.class);
    }

    public List<BookReviewDto> convertToReviewList(List<BookReview> reviews) {
        return reviews
                .stream()
                .map(customer -> mapper.map(customer, BookReviewDto.class))
                .collect(Collectors.toList());
    }
}
