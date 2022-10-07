package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.ImageService;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final OrderService orderService;
    private final MessageSource messageSource;
    private final OrderValidator orderValidator;
    private final ImageService imageService;

    @Autowired
    public ProductController(ProductService productService, OrderService orderService, MessageSource messageSource, OrderValidator orderValidator, ImageService imageService) {
        this.productService = productService;
        this.orderService = orderService;
        this.messageSource = messageSource;
        this.orderValidator = orderValidator;
        this.imageService = imageService;
    }

    @InitBinder("orderDto")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(orderValidator);
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("orderDto") OrderDto orderDto, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                messageSource.getMessage(
                                        "errors.productcontroller.productnotfound",
                                        null,
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );
        orderDto.setProductsAmount(1);
        orderDto.setProductId(product.getId());
        model.addAttribute("product", product);

        return "product/show";
    }

    @PostMapping("/order")
    public String order(@ModelAttribute("orderDto") @Valid OrderDto orderDto, BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Product product = productService.findById(orderDto.getProductId()).orElse(null);
            model.addAttribute("product", product);
            return "product/show";
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute("order", orderService.save(orderDto, userDetails.getUser().getId()));

        return "product/order_complete";
    }
}
