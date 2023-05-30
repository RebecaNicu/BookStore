package com.nagarro.advanced.framework.facade.convertor;

import org.springframework.stereotype.Component;

@Component
public interface Converter<T, E> {

    T toEntity(E object);

    E toDto(T object);
}
