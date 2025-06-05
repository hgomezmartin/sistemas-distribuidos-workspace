package com.ubu.p3.twentyonepilots.controller;


import com.ubu.p3.twentyonepilots.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService users;

    @GetMapping
    public String list(Model m){
        m.addAttribute("users", users.list());
        return "users";
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String role){
        users.updateRole(id, role);   // o users.toggleRole(id);
        return "redirect:/users";
    }
}
