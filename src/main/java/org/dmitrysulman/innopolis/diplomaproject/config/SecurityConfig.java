package org.dmitrysulman.innopolis.diplomaproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {//extends WebSecurityConfigurerAdapter {

//    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userDetailsService)
//                .and().build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig,
//            ObjectPostProcessor<Object> objectObjectPostProcessor,
//            ApplicationContext applicationContext) throws Exception {
//        return authConfig.authenticationManagerBuilder(objectObjectPostProcessor,applicationContext)
//                .userDetailsService(userDetailsService)
//                .and().build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
////        return authenticationManagerBuilder.authenticationProvider(authenticationProvider)
////                .build();
//        return authenticationManagerBuilder.userDetailsService(userDetailsService)
//                .and().build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain securityFilterChains(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authenticationProvider(authenticationProvider).authorizeHttpRequests((a) -> a.anyRequest().authenticated()).httpBasic();
//        httpSecurity.userDetailsService(userDetailsService).authorizeRequests((a) -> a.anyRequest().authenticated()).formLogin().permitAll();
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/signin")
                                .loginProcessingUrl("/process-signin")
                                .permitAll()
                );
        return httpSecurity.build();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //auth.authenticationProvider(authenticationProvider);
//        auth.userDetailsService(userDetailsService);
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
