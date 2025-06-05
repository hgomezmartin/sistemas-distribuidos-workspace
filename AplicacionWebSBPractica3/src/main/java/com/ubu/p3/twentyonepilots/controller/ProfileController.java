package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.User;
import com.ubu.p3.twentyonepilots.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model) {
        User u = (User) auth.getPrincipal();
        model.addAttribute("user", u);
        return "profile";                    // templates/profile.html
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication auth,
                                @RequestParam String email,
                                @RequestParam String password) {

        User u = (User) auth.getPrincipal();
        u.setEmail(email);
        if (!password.isBlank()) {
            u.setPassword(encoder.encode(password));
        }
        repo.save(u);
        return "redirect:/profile?ok";
    }
}
