package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderItemDto;
import org.dmitrysulman.innopolis.diplomaproject.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public CartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("")
    public String cart() {
        return "cart/index";
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Void> order(@RequestBody OrderDto orderDto) {
        for (OrderItemDto orderItemDto : orderDto.getProducts()) {
            System.out.println(orderItemDto.getProductId() + ": " + orderItemDto.getProductsAmount());
        }
        return ResponseEntity.noContent().build();
    }
}
