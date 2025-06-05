package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.Order;
import com.ubu.p3.twentyonepilots.model.User;
import com.ubu.p3.twentyonepilots.repository.OrderRepository;
import com.ubu.p3.twentyonepilots.repository.UserRepository;
import com.ubu.p3.twentyonepilots.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cart;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    @GetMapping
    public String view(Model m){ //muestra el contenido del carrito
        m.addAttribute("items", cart.list());
        m.addAttribute("total", cart.total());
        return "cart";
    }

    //checkout qguarda Order y limpia el carito
    @PostMapping("/checkout")
    public String checkout(Authentication auth){
        if(cart.list().isEmpty()) return "redirect:/cart";

        String username = auth.getName();
        User u = userRepo.findByUsername(username).orElseThrow(); //obntenemos el usuario autenticado

        Order o = new Order(); //onstruimos el pedido
        o.setUser(u);
        o.getItems().addAll(cart.list()); //copiamos lineas
        o.getItems().forEach(i -> i.setOrder(o));

        orderRepo.save(o);
        cart.clear();  //vaciamos carrito
        return "redirect:/?orderOk";
    }

    @PostMapping("/remove/{idx}")
    public String remove(@PathVariable int idx) { //quitamos el pedido que queremos quitar con la pos en la lista
        cart.remove(idx);
        return "redirect:/cart";
    }

}
