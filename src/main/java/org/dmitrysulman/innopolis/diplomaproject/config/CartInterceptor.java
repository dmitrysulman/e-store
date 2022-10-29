package org.dmitrysulman.innopolis.diplomaproject.config;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CartInterceptor implements HandlerInterceptor {
    private final CartService cartService;

    @Autowired
    public CartInterceptor(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        Cart cart;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //TODO DB!!
        //if (authentication instanceof AnonymousAuthenticationToken) {
        if (true) {
            cart = (Cart) request.getSession().getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                request.getSession().setAttribute("cart", cart);
            } else {
                cartService.updateCartContent(cart);
            }
        } else {
            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
            cart = cartService.getCartByUser(user.getId());
            request.getSession().setAttribute("cart", cart);
        }
        if (modelAndView != null) {
            modelAndView.getModelMap().addAttribute("cart", cart);
        }
    }
}
