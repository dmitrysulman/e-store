package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(ProductService productService, OrderService orderService, MessageSource messageSource) {
        this.productService = productService;
        this.orderService = orderService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("order") OrderDto order, Model model) {
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
        order.setProductId(product.getId());
        model.addAttribute("product", product);

        return "product/show";
    }

    @PostMapping("/order")
    public String order(@ModelAttribute("order") @Valid OrderDto orderDto) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        orderService.save(orderDto, user.getId());

        return "redirect:/";
    }
}
