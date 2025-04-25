package com.ubu.p2.users.controller;

import com.ubu.p2.users.model.User;
import com.ubu.p2.users.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(HttpSession session, Model model) {
        // Comprueba si hay un usuario en sesión
        if (session.getAttribute("userId") != null) {
            model.addAttribute("logued", true);
        }
        return "home";  // Plantilla Thymeleaf (home.html)
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    @PostMapping("/checkLogin")
    public String checkLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             Model model,
                             HttpSession session) {

        // 1- si existe un usuario con ese username
        // 2- si el password coincide
        boolean valid = userService.checkRegistered(username, password);
        if (valid) {
            // Recupera el user en cuestión para saber su ID (si quieres guardarlo en sesión)
            User user = userService.findByUsername(username);

            if (user != null) {
                // guardas el userId en sesión o lo que necesites
                session.setAttribute("userId", user.getId());
            }

            model.addAttribute("logued", true);
            return "home";  // si login ok, vuelve a home (o un dashboard, etc.)
        } else {
            model.addAttribute("error", "Usuario o contraseña inválidos");
            return "login";
        }
    }



    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session) {
        Object sessionUserId = session.getAttribute("userId");
        if (sessionUserId == null) {
            //si no hay user en sesión redirige a login
            return "redirect:/login";
        }

        Long userId = (Long) sessionUserId;
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "profile"; // profile.html
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("id") Long id,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("email") String email,
                                Model model) {
        // llamamos a un metodo del service que haga el update
        userService.updateProfile(id, username, password, email);

        // volvemos a cargar los datos para mostrarlos
        User updatedUser = userService.getUserById(id);
        model.addAttribute("user", updatedUser);

        return "profile";  // o redirect a /profil
    }

    @GetMapping("/showUsers")
    public String showUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userList", userList);
        return "showUsers";  // showUsers.html
    }

}
