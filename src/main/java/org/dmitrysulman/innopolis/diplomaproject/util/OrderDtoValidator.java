package org.dmitrysulman.innopolis.diplomaproject.util;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

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
//        OrderDto orderDto = (OrderDto) target;
//        Optional<Product> product = productService.findById(orderDto.getProductId());
//        product.ifPresentOrElse(p -> {
//                    if (orderDto.getProductAmount() > p.getAmount()) {
//                        errors.rejectValue("productAmount", "error.order.toomanyproducts");
//                    }
//                },
//                () -> errors.rejectValue("productAmount", "error.order.productnotfound")
//        );
    }
}
