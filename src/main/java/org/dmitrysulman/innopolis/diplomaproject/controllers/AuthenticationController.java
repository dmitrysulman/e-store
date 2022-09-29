package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.dmitrysulman.innopolis.diplomaproject.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AuthenticationController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @InitBinder
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
    public String signUp(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Authentication authentication) {
        if (authentication != null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "authentication/signup";
        }
        userService.save(user);

        return "redirect:/";
    }
}
