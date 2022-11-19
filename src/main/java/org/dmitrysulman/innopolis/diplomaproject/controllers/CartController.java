package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.AddToCartDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.OrderSuccessDto;
import org.dmitrysulman.innopolis.diplomaproject.dto.RemoveFromCartDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.Order;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.OrderService;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderErrorResponse;
import org.dmitrysulman.innopolis.diplomaproject.util.OrderDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.Instant;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final OrderService orderService;
    private final CartService cartService;
    private final OrderDtoValidator orderDtoValidator;

    @Autowired
    public CartController(OrderService orderService, CartService cartService, OrderDtoValidator orderDtoValidator) {
        this.orderService = orderService;
        this.cartService = cartService;
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

    @PostMapping("/add_to_cart")
    @ResponseBody
    public ResponseEntity<HttpStatus> addToCart(@RequestBody @Valid AddToCartDto addToCartDto,
                                                HttpSession httpSession,
                                                Authentication authentication) throws ElementNotFoundException {
        int productId = addToCartDto.getProductId();
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            cartService.addProductToCart(user.getId(), productId);
        } else {
            cartService.addProductToCart((Cart) httpSession.getAttribute("cart"), productId);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/remove_from_cart")
    @ResponseBody
    public ResponseEntity<HttpStatus> removeFromCart(@RequestBody @Valid RemoveFromCartDto removeFromCartDto,
                                                HttpSession httpSession,
                                                Authentication authentication) throws ElementNotFoundException {
        int productId = removeFromCartDto.getProductId();
        boolean completely = removeFromCartDto.isCompletely();
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            cartService.removeProductFromCart(user.getId(), productId, completely);
        } else {
            cartService.removeProductFromCart((Cart) httpSession.getAttribute("cart"), productId, completely);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<OrderSuccessDto> order(@RequestBody @Valid OrderDto orderDto,
                                                 Authentication authentication) throws ElementNotFoundException {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        Order order = orderService.save(orderDto, user.getId());
        OrderSuccessDto orderSuccessDto = new OrderSuccessDto(order.getId());
        cartService.clearCart(user.getId());

        return ResponseEntity.ok(orderSuccessDto);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<OrderErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        OrderErrorResponse orderErrorResponse = new OrderErrorResponse(status.getReasonPhrase(), Instant.now(), status.value());
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            if (violation.getLeafBean() instanceof Product) {
                orderErrorResponse.addProductError(((Product) violation.getRootBean()).getId(), violation.getMessage());
            } else {
                orderErrorResponse.addValidationError(new FieldError(violation.getRootBeanClass().getSimpleName(),
                        violation.getPropertyPath().toString(),
                        violation.getMessage())
                );
            }
        }

        return new ResponseEntity<>(orderErrorResponse, status);
    }
}
