package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderSuccessDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderErrorResponse;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.Instant;

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
    public ResponseEntity<OrderSuccessDto> order(@RequestBody @Valid OrderDto orderDto,
                                                 Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Order order = orderService.save(orderDto, userDetails.getUser().getId());
        OrderSuccessDto orderSuccessDto = new OrderSuccessDto(order.getId());
        return ResponseEntity.ok(orderSuccessDto);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<OrderErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        OrderErrorResponse orderErrorResponse = new OrderErrorResponse(status.getReasonPhrase(), Instant.now(), status.value());
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            if (violation.getLeafBean() instanceof Product) {
                orderErrorResponse.addProductError(((Product) violation.getLeafBean()).getId(), violation.getMessage());
            }
        }

        return new ResponseEntity<>(orderErrorResponse, new HttpHeaders(), status);
    }
}
