package org.dmitrysulman.innopolis.diplomaproject.config;

import org.dmitrysulman.innopolis.diplomaproject.models.ShoppingCart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ShoppingCartAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartAuthenticationSuccessHandler(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
        setTargetUrlParameter("redirect");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("cart");
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        shoppingCartService.setShoppingCartWithUserAfterLogin(user, shoppingCart);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
