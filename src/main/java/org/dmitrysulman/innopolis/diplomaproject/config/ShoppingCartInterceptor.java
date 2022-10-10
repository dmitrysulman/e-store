package org.dmitrysulman.innopolis.diplomaproject.config;

import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("cart");
        if (shoppingCart == null) {
            shoppingCart = shoppingCartService.getShoppingCart();
            request.getSession().setAttribute("cart", shoppingCart);
        }
    }
}
