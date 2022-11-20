package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.models.Product;
import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.dmitrysulman.innopolis.diplomaproject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CartService cartService;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(ProductService productService,
                             CartService cartService, MessageSource messageSource) {
        this.productService = productService;
        this.cartService = cartService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model,
                       HttpSession httpSession,
                       Authentication authentication) {
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
        Cart cart;
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            cart = cartService.getCartByUser(user.getId());
        } else {
            cart = (Cart) httpSession.getAttribute("cart");
        }

        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == id)
                .findFirst()
                .ifPresent(cartItem -> model.addAttribute("amountInCart", cartItem.getProductAmount()));
        model.addAttribute("product", product);

        return "product/show";
    }
}