package com.ubu.p3.twentyonepilots.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController { //devuelce el home
    @GetMapping("/") String home(){
        return "index";
    }
}
