package org.dmitrysulman.innopolis.diplomaproject.config;

import org.dmitrysulman.innopolis.diplomaproject.models.Cart;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.CartService;
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
public class CartAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {
    private final CartService cartService;

    @Autowired
    public CartAuthenticationSuccessHandler(CartService cartService) {
        this.cartService = cartService;
        setTargetUrlParameter("redirect");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        cartService.mergeCartAfterLogin(cart, user.getId());
        request.getSession().removeAttribute("cart");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
