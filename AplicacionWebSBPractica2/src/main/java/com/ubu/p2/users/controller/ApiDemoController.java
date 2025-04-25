package com.ubu.p2.users.controller;

import com.ubu.p2.users.service.FlaskClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api-demo")
public class ApiDemoController {

    private final FlaskClientService api;

    public ApiDemoController(FlaskClientService api) {
        this.api = api;
    }

    //muestra ek formulario vacio
    @GetMapping
    public String form() {
        return "apiDemo";     // templates/apiDemo.html
    }

    //recibe lo que el usuario quiere invocar
    @PostMapping("/call")
    public String call(@RequestParam String endpoint, Model model) {
        try {
            Object result = api.get(endpoint);
            model.addAttribute("result", result);
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "apiDemo";
    }
}
