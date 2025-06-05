package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.Ticket;
import com.ubu.p3.twentyonepilots.service.CartService;
import com.ubu.p3.twentyonepilots.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    @Autowired
    private final TicketService srv;
    private final CartService cart;

    @GetMapping
    public String list(Model model){
        model.addAttribute("tickets", srv.list());
        return "tickets/list";
    }

    //CRUD admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newForm(Model m){ m.addAttribute("ticket", new Ticket());
        return "tickets/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model m){
        m.addAttribute("ticket", srv.get(id));
        return "tickets/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid Ticket t, BindingResult br){ //implementacion del valid
        if(br.hasErrors()) return "tickets/form";
        srv.save(t);
        return "redirect:/tickets";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        srv.delete(id);
        return "redirect:/tickets";
    }

    //añadir al carrito
    @PostMapping("/{id}/add")
    public String addToCart(@PathVariable Long id){
        cart.addTicket(srv.get(id));
        return "redirect:/cart";
    }
}
