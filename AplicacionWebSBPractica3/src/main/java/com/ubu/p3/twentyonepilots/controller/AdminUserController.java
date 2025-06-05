package com.ubu.p3.twentyonepilots.controller;


import com.ubu.p3.twentyonepilots.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * CRUD minimo de usuarioos solo accesible para el admin (ROLE_ADMIN)
 */

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService users;

    @GetMapping
    public String list(Model m){ //lista de usuarios
        m.addAttribute("users", users.list());
        return "users"; // templates/users.html
    }

    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Long id, @RequestParam String role){ //cambia de rol
        users.updateRole(id, role);
        return "redirect:/users"; // recarga la tabla
    }
}
