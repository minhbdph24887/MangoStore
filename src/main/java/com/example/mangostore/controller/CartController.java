package com.example.mangostore.controller;

import com.example.mangostore.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mangostore/")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "cart")
    public String cartIndex(Model model,
                            HttpSession session) {
        return cartService.cartIndex(model, session);
    }
}
