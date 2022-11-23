package org.dmitrysulman.innopolis.diplomaproject.util;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrderDtoValidator implements Validator {
    private final ProductService productService;

    @Autowired
    public OrderDtoValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
