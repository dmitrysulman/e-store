package org.dmitrysulman.innopolis.diplomaproject.config;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object shoppingCart = request.getSession().getAttribute("cart");
        if (shoppingCart == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                request.getSession().setAttribute("cart", shoppingCartService.getShoppingCart());
            } else {
                User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
                request.getSession().setAttribute("cart", shoppingCartService.getShoppingCart(user));
            }
        }

        return true;
    }
}
