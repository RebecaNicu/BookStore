package com.nagarro.advanced.framework.facade.convertor.impl;

import com.nagarro.advanced.framework.controller.model.CartDto;
import com.nagarro.advanced.framework.persistence.entity.Cart;
import lombok.RequiredArgsConstructor;
import com.nagarro.advanced.framework.facade.convertor.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMapper implements Converter<Cart, CartDto> {
    private final ModelMapper modelMapper;

    public Cart toEntity(CartDto object) {
        return modelMapper.map(object, Cart.class);
    }

    public CartDto toDto(Cart object) {
        return modelMapper.map(object, CartDto.class);
    }
}
