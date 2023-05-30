package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.CategoryDto;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import com.nagarro.advanced.framework.persistence.entity.Category;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper implements Converter<Category, CategoryDto> {

    private ModelMapper modelMapper;

    @Autowired
    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Category toEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }

    @Override
    public CategoryDto toDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
