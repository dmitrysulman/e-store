package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.dmitrysulman.innopolis.diplomaproject.dto.UserDto;
import org.dmitrysulman.innopolis.diplomaproject.models.User;
import org.dmitrysulman.innopolis.diplomaproject.security.UserDetailsImpl;
import org.dmitrysulman.innopolis.diplomaproject.services.UserService;
import org.dmitrysulman.innopolis.diplomaproject.util.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final UserDtoValidator userDtoValidator;

    @Autowired
    public ProfileController(UserService userService, UserDtoValidator userDtoValidator) {
        this.userService = userService;
        this.userDtoValidator = userDtoValidator;
    }

    @InitBinder("userDto")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(userDtoValidator);
    }

    @GetMapping("")
    public String show(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute("user", userDetails.getUser());

        return "profile/show";
    }

    @GetMapping("/edit")
    public String edit(@ModelAttribute("userDto") UserDto userDto, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userDto.setFirstName(userDetails.getUser().getFirstName());
        userDto.setSecondName(userDetails.getUser().getSecondName());
        userDto.setEmail(userDetails.getUser().getEmail());
        userDto.setAddress(userDetails.getUser().getAddress());

        return "profile/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "profile/edit";
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        user = userService.update(userDto, user.getId());

        userDetails = new UserDetailsImpl(user);
        authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/profile";
    }

    @GetMapping("/orders")
    private String orders(Model model, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        model.addAttribute("orders", userDetails.getUser().getOrders());

        return "profile/orders";
    }
}
