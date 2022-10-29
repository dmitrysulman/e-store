package org.dmitrysulman.innopolis.diplomaproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    private final CartInterceptor cartInterceptor;

    @Autowired
    public InterceptorConfig(CartInterceptor cartInterceptor) {
        this.cartInterceptor = cartInterceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor);
    }
}
