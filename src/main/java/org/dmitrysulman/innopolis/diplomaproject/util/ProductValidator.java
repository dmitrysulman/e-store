package org.dmitrysulman.innopolis.diplomaproject.util;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ProductValidator implements Validator {
    private final ProductService productService;

    public ProductValidator(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        Optional<Product> productWithSameName = productService.findByName(product.getName());
        if (productWithSameName.isPresent() && product.getId() != productWithSameName.get().getId()) {
            errors.rejectValue("name", "error.product.name.namealreadyexist");
        }

        long nonEmptyImagesCount = Arrays.stream(product.getImages())
                .filter(multipartFile -> !multipartFile.isEmpty())
                .count();
        if (nonEmptyImagesCount == 0L) {
            errors.rejectValue("images", "error.product.images.noimages");
        }

    }
}
