package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.dmitrysulman.innopolis.diplomaproject.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AuthenticationController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;


    @Autowired
    public AuthenticationController(UserService userService, UserValidator userValidator, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @InitBinder("user")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping("/signin")
    public String signInPage(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }

        return "authentication/signin";
    }

    @GetMapping("/signup")
    public String signUpPage(@ModelAttribute("user") User user, Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }

        return "authentication/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         HttpServletRequest request,
                         Authentication authentication) throws ServletException {
        if (authentication != null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "authentication/signup";
        }
        String password = user.getPassword();
        userService.save(user);
        request.login(user.getEmail(),password);
        return "redirect:/" + request.getParameter("redirect");
    }
}
