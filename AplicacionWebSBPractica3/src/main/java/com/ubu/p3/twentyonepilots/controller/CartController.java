package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.Order;
import com.ubu.p3.twentyonepilots.repository.OrderRepository;
import com.ubu.p3.twentyonepilots.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cart;
    private final OrderRepository orderRepo;

    @GetMapping
    public String view(Model m){
        m.addAttribute("items", cart.list());
        m.addAttribute("total", cart.total());
        return "cart";
    }

    //checkout qguarda Order y limpia el carito
    @PostMapping("/checkout")
    public String checkout(Authentication auth){
        if(cart.list().isEmpty()) return "redirect:/cart";

        com.ubu.p3.twentyonepilots.model.User u =
                (com.ubu.p3.twentyonepilots.model.User) auth.getPrincipal();

        Order o = new Order();
        o.setUser(u);
        o.getItems().addAll(cart.list());
        o.getItems().forEach(i -> i.setOrder(o));

        orderRepo.save(o);
        cart.clear();
        return "redirect:/?orderOk";
    }
}
