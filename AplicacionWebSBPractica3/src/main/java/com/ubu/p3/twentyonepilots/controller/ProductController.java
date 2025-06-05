package com.ubu.p3.twentyonepilots.controller;

import com.ubu.p3.twentyonepilots.model.Product;
import com.ubu.p3.twentyonepilots.service.CartService;
import com.ubu.p3.twentyonepilots.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CRUD de merchandising.
 *  – /merch  lo ve todo ek mundo público
 *  – /merch/new, /save, /edit, /delete  -> solo ADMIN (no me ha dado tiempo a implementarlo en las vistas)
 */

@Controller
@RequestMapping("/merch")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService srv;
    private final CartService cart;

    //listado público
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", srv.list());
        return "merchandise";
    }

    // crear / editar (solo admin) - no me ha dado tiempo a implemaentarlo en la vista
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/new")
    public String newForm(Model m) {
        m.addAttribute("product", new Product());
        return "merchandise/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/edit") //editar el producto
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("product", srv.get(id));
        return "merchandise/form";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save") //guardamos
    public String save(@Valid Product product, BindingResult br) { //@valid implementacion
        if (br.hasErrors()) return "merchandise/form";
        srv.save(product);
        return "redirect:/merch";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete") //eliminamos producto
    public String delete(@PathVariable Long id) {
        srv.delete(id);
        return "redirect:/merch";
    }

    //a ñadir al carrito
    @PostMapping("/{id}/add")
    public String addToCart(@PathVariable Long id) {
        cart.addProduct(srv.get(id));
        return "redirect:/cart";
    }
}
