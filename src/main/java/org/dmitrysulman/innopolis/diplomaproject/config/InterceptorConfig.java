package org.dmitrysulman.innopolis.diplomaproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    private final ShoppingCartInterceptor shoppingCartInterceptor;

    @Autowired
    public InterceptorConfig(ShoppingCartInterceptor shoppingCartInterceptor) {
        this.shoppingCartInterceptor = shoppingCartInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shoppingCartInterceptor);
    }
}
