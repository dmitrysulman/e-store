package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.AddToCartDto;
import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.dmitrysulman.innopolis.diplomaproject.util.ElementNotFoundException;
import org.dmitrysulman.innopolis.diplomaproject.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.time.Instant;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(ProductService productService,
                             CartService cartService,
                             MessageSource messageSource) {
        this.productService = productService;
        this.cartService = cartService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model,
                       HttpSession httpSession) {
        Product product = productService.findByIdWithImagesUrls(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                messageSource.getMessage(
                                        "errors.productcontroller.productnotfound",
                                        null,
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );
        model.addAttribute("product", product);

        return "product/show";
    }

    @PostMapping("/add_to_cart")
    @ResponseBody
    public ResponseEntity<HttpStatus> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                     HttpSession httpSession,
                                                     Authentication authentication) throws ElementNotFoundException {
        int productId = addToCartDto.getProductId();
        User user = null;
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            user = userDetails.getUser();
            cartService.addProductToCart(user.getId(), productId);
        } else {
            cartService.addProductToCart((Cart) httpSession.getAttribute("cart"), productId);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler({ElementNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleElementNotFound(ElementNotFoundException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), Instant.now(), status.value());

        return new ResponseEntity<>(errorResponse, status);
    }
}