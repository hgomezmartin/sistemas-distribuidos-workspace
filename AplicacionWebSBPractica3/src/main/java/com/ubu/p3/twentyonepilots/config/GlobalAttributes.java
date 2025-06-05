package com.ubu.p3.twentyonepilots.config;

import com.ubu.p3.twentyonepilots.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAttributes {

    private final CartService cart; //traemos carrito

    //devolvemos el num de items en el carrtio
    @ModelAttribute("cartCount")
    public int cartCount() {
        return cart.list().size();
    }
}
