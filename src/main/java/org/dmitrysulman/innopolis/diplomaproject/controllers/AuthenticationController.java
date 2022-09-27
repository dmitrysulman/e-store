package org.dmitrysulman.innopolis.diplomaproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/signin")
    public String signInPage() {
        return "authentication/signin";
    }
}
