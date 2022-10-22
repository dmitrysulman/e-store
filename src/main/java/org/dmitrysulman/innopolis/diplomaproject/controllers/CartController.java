package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderItemDto;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final OrderService orderService;
    private final OrderDtoValidator orderDtoValidator;

    @Autowired
    public CartController(OrderService orderService, OrderDtoValidator orderDtoValidator) {
        this.orderService = orderService;
        this.orderDtoValidator = orderDtoValidator;
    }

    @InitBinder("orderDto")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(orderDtoValidator);
    }

    @GetMapping("")
    public String cart() {
        return "cart/index";
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<Void> order(@RequestBody @Valid OrderDto orderDto,
                                      Authentication authentication) {
        for (OrderItemDto orderItemDto : orderDto.getOrderProducts()) {
            System.out.println(orderItemDto.getProductId() + ": " + orderItemDto.getProductAmount());
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        orderService.save(orderDto, userDetails.getUser().getId());
//        model.addAttribute("order", orderService.save(orderDto, userDetails.getUser().getId()));
        return ResponseEntity.noContent().build();
    }
}
