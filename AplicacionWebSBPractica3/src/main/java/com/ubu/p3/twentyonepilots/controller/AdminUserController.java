package com.ubu.p3.twentyonepilots.controller;


import com.ubu.p3.twentyonepilots.repository.UserRepository;
import com.ubu.p3.twentyonepilots.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserRepository repo;
    private final UserService srv;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", repo.findAll());
        return "users";  // templates/users.html
    }

    /** cambia ROLE_USER ↔ ROLE_ADMIN */
    @PostMapping("/{id}/toggle")
    public String toggleRole(@PathVariable Long id) {
        srv.toggleRole(id);
        return "redirect:/admin/users";
    }
}
