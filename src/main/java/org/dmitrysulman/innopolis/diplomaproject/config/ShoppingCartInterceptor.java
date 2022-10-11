package org.dmitrysulman.innopolis.diplomaproject.config;

import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ShoppingCartInterceptor implements HandlerInterceptor {
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartInterceptor(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        ShoppingCart shoppingCart;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            shoppingCart = (ShoppingCart) request.getSession().getAttribute("cart");
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                request.getSession().setAttribute("cart", shoppingCart);
            } else {
                shoppingCartService.updateCartContent(shoppingCart);
            }
        } else {
            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
            shoppingCart = shoppingCartService.getShoppingCartByUser(user);
            request.getSession().setAttribute("cart", shoppingCart);
        }
        if (modelAndView != null) {
            modelAndView.getModelMap().addAttribute("cart", shoppingCart);
        }
    }
}
