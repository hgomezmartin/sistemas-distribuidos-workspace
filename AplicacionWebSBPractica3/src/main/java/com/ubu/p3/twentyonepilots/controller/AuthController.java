package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.User;
import com.ubu.p3.twentyonepilots.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @GetMapping("/login")
    public String loginForm() { //login
        return "login"; // templates/login.html
    }


    @GetMapping("/register")
    public String registerForm(Model model) {  //registro
        model.addAttribute("user", new User()); //objeto vacio para el form
        return "register"; // templates/register.html
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email) {

        userService.register(username, password, email);
        return "redirect:/login?registered"; //muestra alert
    }

    @GetMapping("/logout-success")
    public String logoutPage() { //logout ok
        return "logout"; // templates/logout.html
    }
}

